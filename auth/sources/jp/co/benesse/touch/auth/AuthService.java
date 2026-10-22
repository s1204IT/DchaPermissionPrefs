package jp.co.benesse.touch.auth;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jp.co.benesse.dcha.databox.ISbox;
import jp.co.benesse.touch.comm.ICommService;
import jp.co.benesse.touch.common.touchconstant.AuthRequest;
import jp.co.benesse.touch.common.touchconstant.CommonDataBox;
import jp.co.benesse.touch.common.touchconstant.IntentName;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthService extends Service {
    public static final String APP_ID = "app_id";
    public static final String AUTHID = "authid";
    private static final int BIND_CHECK_RETRY_COUNT = 50;
    private static final int BIND_CHECK_RETRY_SLEEP_TIME = 100;
    public static final String BIND_TYPE = "BindType";
    public static final int BIND_TYPE_IAUTHGETUSERID = 1;
    public static final int BIND_TYPE_NORMAL = 0;
    public static final String COMPLETE_NORMAL = "00";
    public static final String CREATE_SESSION = "create_session";
    private static final int GET_AKAMAI_TOKEN_KEY = 5;
    private static final int GET_ONE_TIME_KEY = 4;
    private static final int GET_SESSION_KEY = 2;
    public static final String ID = "id";
    private static final String INIT_PREF_NAME = "init";
    private static final String IS_INIT_FLAG = "isinitialized";
    public static final String LOGINID = "loginid";
    public static final String NAME = "name";
    public static final String ONETIMEID = "onetimeid";
    public static final long RESPONSE_WAIT_TIMEOUT_MS = 100000;
    public static final String RESULT_ERROR = "ERROR";
    public static final String RESULT_NG = "NG";
    public static final String RESULT_OK = "OK";
    public static final String SBOX_INTENT = "jp.co.benesse.dcha.databox.Sbox";
    public static final String SBOX_INTENT_PACKAGE = "jp.co.benesse.dcha.databox";
    public static final String SECURE = "secure";
    public static final String SECURE_FALSE = "false";
    public static final String SECURE_TRUE = "true";
    public static final String SERVICE_CODE = "service_code";
    private static final String SERVICE_CODE_VALUE = "benesse";
    public static final String SESSION = "session";
    public static final String SESSIONID = "sessionid";
    public static final String TAG = "AuthService";
    public static final String UPDATETIME = "updatetime";
    private static final int UPDATE_CONTRACT_INFO = 1;
    private static final int UPDATE_SESSION_KEY = 3;
    public static final String USERID = "userid";
    public static final String USER_NAME = "user_name";
    public static final String USER_PASSWORD = "user_password";
    private static final long WORKING_LOCK_TIMEOUT = 5000;
    private static final long WORKING_LOCK_TIMEOUT_DEBUG = 30000;
    public static final String XML_PARSE_ERROR = "-1";
    ICommService commService;
    private Handler handler;
    private static LockCancelTask lockCancelTask = null;
    private static boolean workingFlag = false;
    private static String AUTH_HANDLER_NAME = "AuthServiceHandler";
    ISbox isbox = null;
    boolean isCommServiceDisconnected = false;
    final Lock responseWaitlock = new ReentrantLock();
    final Condition responseWaitCond = this.responseWaitlock.newCondition();
    private ServiceConnection commServiceConnection = new CommServiceConnection();
    private ServiceConnection sboxConnection = new SboxConnection();
    HandlerThread handlerThread = null;
    private IAuthService.Stub authServiceStub = new IAuthService.Stub() {
        @Override
        @SuppressLint({"NewApi"})
        public void authUser(String userId, String password) throws RemoteException {
            try {
                try {
                    AuthService.this.releaseWorkingLockTimeout();
                    if (userId == null || password == null) {
                        throw new RemoteException("UserID or Password is null.");
                    }
                    String appId = SboxAdapter.getAppIdentifier();
                    AuthKeyRequest authReq = new AuthKeyRequest();
                    Map<String, String> postParams = new HashMap<>();
                    postParams.put(AuthService.USER_NAME, userId);
                    postParams.put(AuthService.USER_PASSWORD, password);
                    postParams.put(AuthService.APP_ID, appId);
                    postParams.put(AuthService.SERVICE_CODE, AuthService.SERVICE_CODE_VALUE);
                    postParams.put(AuthService.CREATE_SESSION, "ON");
                    authReq.setPostParams(postParams);
                    AuthKeyResponse authRes = new AuthKeyResponse(AuthService.this);
                    ICommService commService = AuthService.this.getCommService();
                    if (commService != null) {
                        commService.requestComm2AuthServer(authReq, authRes);
                        AuthService.this.finishRequest();
                        return;
                    }
                    throw new RemoteException("commService is null.");
                } catch (RemoteException e) {
                    throw new RemoteException();
                } catch (Exception e2) {
                    throw new RemoteException();
                }
            } catch (Throwable th) {
                AuthService.this.finishRequest();
                throw th;
            }
        }

        @Override
        public void updateContractInfo(IAuthCallback callback) throws RemoteException {
            AuthService.this.releaseWorkingLockTimeout();
            Message msg = AuthService.this.handler.obtainMessage();
            msg.what = 1;
            msg.obj = callback;
            AuthService.this.handler.sendMessage(msg);
        }

        @Override
        public void getSessionKey(boolean secure, IAuthCallback callback) throws RemoteException {
            Message msg = AuthService.this.handler.obtainMessage();
            msg.what = 2;
            msg.obj = AuthService.this.new SessionKeySet(secure, callback);
            AuthService.this.handler.sendMessage(msg);
        }

        @Override
        @SuppressLint({"NewApi"})
        public String getSessionHeaderName(boolean secure) throws RemoteException {
            try {
                JSONArray sessions = SboxAdapter.getInstance().getArrayValues(AuthRequest.SBOX_SESSION);
                if (sessions == null) {
                    return null;
                }
                for (int i = 0; i < sessions.length(); i++) {
                    JSONObject data = sessions.getJSONObject(i);
                    if (!data.has(AuthService.SECURE) || !data.has(AuthService.NAME) || !data.has("id")) {
                        throw new RemoteException("getSessionKey data is null");
                    }
                    if (data.getString(AuthService.SECURE).equals(AuthService.SECURE_TRUE) && secure) {
                        String headerName = data.getString(AuthService.NAME);
                        return headerName;
                    }
                    if (data.getString(AuthService.SECURE).equals(AuthService.SECURE_FALSE) && !secure) {
                        String headerName2 = data.getString(AuthService.NAME);
                        return headerName2;
                    }
                }
                return null;
            } catch (JSONException e) {
                return null;
            }
        }

        @Override
        public void updateSessionKey(boolean secure, IAuthCallback callback) throws RemoteException {
            AuthService.this.releaseWorkingLockTimeout();
            Message msg = AuthService.this.handler.obtainMessage();
            msg.what = 3;
            msg.obj = AuthService.this.new SessionKeySet(secure, callback);
            AuthService.this.handler.sendMessage(msg);
        }

        @Override
        public void getOneTimeKey(IAuthCallback callback) throws RemoteException {
            AuthService.this.releaseWorkingLockTimeout();
            Message msg = AuthService.this.handler.obtainMessage();
            msg.what = 4;
            msg.obj = callback;
            AuthService.this.handler.sendMessage(msg);
        }

        @Override
        public void getAkamaiToken(String url, List<String> accessList, IAuthCallback callback) {
            AuthService.this.releaseWorkingLockTimeout();
            Message msg = AuthService.this.handler.obtainMessage();
            msg.what = 5;
            msg.obj = AuthService.this.new AkamaiTokenSet(url, accessList, callback);
            AuthService.this.handler.sendMessage(msg);
        }

        @Override
        public boolean deleteAuthInfo() throws RemoteException {
            try {
                SboxAdapter sbox = SboxAdapter.getInstance();
                sbox.setValue(AuthRequest.SBOX_AUTH_KEY, "");
                JSONArray jsonArray = sbox.getArrayValues(AuthRequest.SBOX_SESSION);
                try {
                    int n = jsonArray.length();
                    for (int i = 0; i < n; i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        object.put("id", "");
                    }
                    sbox.setArrayValues(AuthRequest.SBOX_SESSION, jsonArray);
                    return true;
                } catch (JSONException e) {
                    return false;
                }
            } catch (Exception e2) {
                throw new RemoteException();
            }
        }

        @Override
        public boolean lockAuthWorking() {
            if (!AuthService.workingFlag) {
                LockCancelTask unused = AuthService.lockCancelTask = AuthService.this.new LockCancelTask();
                AuthService.this.handler.postDelayed(AuthService.lockCancelTask, AuthService.WORKING_LOCK_TIMEOUT);
                boolean unused2 = AuthService.workingFlag = true;
                return true;
            }
            return false;
        }

        @Override
        public String getStringValue(String key) throws RemoteException {
            return SboxAdapter.getInstance().getValue(key);
        }

        @Override
        public String getArrayValues(String key) throws RemoteException {
            return SboxAdapter.getInstance().getArrayValues(key).toString();
        }

        @Override
        public void setStringValue(String key, String value) throws RemoteException {
            SboxAdapter.getInstance().setValue(key, value);
        }

        @Override
        public void setArrayValues(String key, String values) throws RemoteException {
            try {
                JSONArray jsonArray = new JSONArray(values);
                SboxAdapter.getInstance().setArrayValues(key, jsonArray);
            } catch (JSONException e) {
                throw new RemoteException();
            }
        }

        @Override
        public String getAppIdentifier() throws RemoteException {
            return SboxAdapter.getAppIdentifier();
        }

        @Override
        public String getUserId() throws RemoteException {
            return SboxAdapter.getInstance().getValue(AuthRequest.SBOX_USER_ID);
        }

        @Override
        public String getHttpsSessionKey() throws RemoteException {
            try {
                JSONArray session = SboxAdapter.getInstance().getArrayValues(AuthRequest.SBOX_SESSION);
                for (int i = 0; i < session.length(); i++) {
                    JSONObject obj = (JSONObject) session.get(i);
                    if (obj.getBoolean(AuthService.SECURE)) {
                        String sessionKey = obj.getString("id");
                        return sessionKey;
                    }
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    };
    private IAuthGetUserId.Stub iAuthGetUserId = new IAuthGetUserId.Stub() {
        @Override
        public String getUserId() throws RemoteException {
            return SboxAdapter.getInstance().getValue(AuthRequest.SBOX_USER_ID);
        }

        @Override
        public String getSessionKey() throws RemoteException {
            try {
                JSONArray session = SboxAdapter.getInstance().getArrayValues(AuthRequest.SBOX_SESSION);
                for (int i = 0; i < session.length(); i++) {
                    JSONObject obj = (JSONObject) session.get(i);
                    if (obj.getBoolean(AuthService.SECURE)) {
                        String sessionKey = obj.getString("id");
                        return sessionKey;
                    }
                }
                return null;
            } catch (Exception e) {
                return null;
            }
        }
    };

    class CommServiceConnection implements ServiceConnection {
        CommServiceConnection() {
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            AuthService.this.commService = ICommService.Stub.asInterface(service);
            AuthService.this.isCommServiceDisconnected = false;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            AuthService.this.commService = null;
            AuthService.this.isCommServiceDisconnected = true;
        }
    }

    class SboxConnection implements ServiceConnection {
        SboxConnection() {
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            AuthService.this.releaseWorkingLockTimeout();
            AuthService.this.isbox = ISbox.Stub.asInterface(service);
            AuthService.this.copySboxValue();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            AuthService.this.isbox = null;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        workingFlag = false;
        if (this.handlerThread == null) {
            this.handlerThread = new HandlerThread(AUTH_HANDLER_NAME, 10);
            this.handlerThread.start();
            Looper looper = this.handlerThread.getLooper();
            this.handler = new HandlerExtension(looper, this);
        }
        init();
        Intent commIntent = new Intent(IntentName.ACTION_COMM_SERVICE);
        commIntent.setPackage(IntentName.PKG_COMM_SERVICE);
        bindService(commIntent, this.commServiceConnection, 1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.commServiceConnection != null) {
            if (this.commService != null) {
                unbindService(this.commServiceConnection);
                this.commService = null;
            }
            this.commServiceConnection = null;
        }
        if (this.sboxConnection != null) {
            if (this.isbox != null) {
                unbindService(this.sboxConnection);
                this.isbox = null;
            }
            this.sboxConnection = null;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        if (arg0 != null) {
            int type = arg0.getIntExtra(BIND_TYPE, 0);
            switch (type) {
                case 1:
                    return this.iAuthGetUserId;
            }
        }
        return this.authServiceStub;
    }

    protected String getAuthKey() throws RemoteException {
        return SboxAdapter.getInstance().getValue(AuthRequest.SBOX_AUTH_KEY);
    }

    protected void getSessionkey(boolean secure, IAuthCallback callback) {
        Message msg = this.handler.obtainMessage();
        msg.what = 2;
        msg.obj = new SessionKeySet(secure, callback);
        this.handler.sendMessage(msg);
    }

    private static class HandlerExtension extends Handler {
        private final WeakReference<AuthService> service;

        HandlerExtension(Looper looper, AuthService service) {
            super(looper);
            this.service = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            AuthService authService = this.service.get();
            if (authService != null) {
                try {
                    switch (msg.what) {
                        case 1:
                            if (msg.obj instanceof IAuthCallback) {
                                IAuthCallback callback = (IAuthCallback) msg.obj;
                                new Thread(new ContractInfoUpdateThread(authService, callback)).start();
                            }
                            break;
                        case 2:
                            if (msg.obj instanceof SessionKeySet) {
                                IAuthCallback callback2 = ((SessionKeySet) msg.obj).callback;
                                boolean secure = ((SessionKeySet) msg.obj).secure;
                                new Thread(new SessionKeyGetThread(authService, secure, callback2)).start();
                            }
                            break;
                        case 3:
                            if (msg.obj instanceof SessionKeySet) {
                                IAuthCallback callback3 = ((SessionKeySet) msg.obj).callback;
                                boolean secure2 = ((SessionKeySet) msg.obj).secure;
                                new Thread(new SessionKeyUpdateThread(authService, secure2, callback3)).start();
                            }
                            break;
                        case 4:
                            if (msg.obj instanceof IAuthCallback) {
                                IAuthCallback callback4 = (IAuthCallback) msg.obj;
                                new Thread(new OnetimekeyGetThread(authService, callback4)).start();
                            }
                            break;
                        case 5:
                            if (msg.obj instanceof AkamaiTokenSet) {
                                AkamaiTokenSet obj = (AkamaiTokenSet) msg.obj;
                                IAuthCallback callback5 = obj.callback;
                                String url = obj.url;
                                List<String> accessList = obj.accessList;
                                new Thread(new AkamaiTokenGetThread(url, accessList, authService, callback5)).start();
                            }
                            break;
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    protected ICommService getCommService() {
        if (this.commService == null) {
            if (this.isCommServiceDisconnected) {
                this.commServiceConnection = new CommServiceConnection();
                Intent intent = new Intent(IntentName.ACTION_COMM_SERVICE);
                intent.setPackage(IntentName.PKG_COMM_SERVICE);
                bindService(intent, this.commServiceConnection, 1);
            }
            for (int count = 0; count < BIND_CHECK_RETRY_COUNT && this.commService == null; count++) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                }
            }
        }
        return this.commService;
    }

    private void init() {
        SharedPreferences pref = getSharedPreferences(INIT_PREF_NAME, 0);
        boolean isInitialized = pref.getBoolean(IS_INIT_FLAG, false);
        if (!isInitialized) {
            lockCancelTask = new LockCancelTask();
            this.handler.postDelayed(lockCancelTask, WORKING_LOCK_TIMEOUT);
            workingFlag = true;
            Intent intent = new Intent(SBOX_INTENT);
            intent.setPackage(SBOX_INTENT_PACKAGE);
            bindService(intent, this.sboxConnection, 1);
        }
    }

    private boolean copySboxValue() {
        boolean ret = false;
        try {
            String authKey = this.isbox.getStringValue(AuthRequest.SBOX_AUTH_KEY);
            String userId = this.isbox.getStringValue(AuthRequest.SBOX_USER_ID);
            String httpSessionHeaderName = getPreinSessionHeaderName(this.isbox, AuthRequest.SBOX_SESSION, false);
            String httpsSessionHeaderName = getPreinSessionHeaderName(this.isbox, AuthRequest.SBOX_SESSION, true);
            String httpSessionKey = getPreinSessionKey(this.isbox, AuthRequest.SBOX_SESSION, false);
            String httpsSessionKey = getPreinSessionKey(this.isbox, AuthRequest.SBOX_SESSION, true);
            String gradeCode = this.isbox.getStringValue(AuthRequest.SBOX_GRADE_CODE);
            String cdnRoot = this.isbox.getStringValue("cdn_root_url");
            String line = this.isbox.getStringValue("line");
            JSONArray arrayHb = new JSONArray();
            JSONObject httpElem = new JSONObject();
            JSONObject httpsElem = new JSONObject();
            httpElem.put(SECURE, false);
            httpElem.put("id", httpSessionKey);
            httpElem.put(NAME, httpSessionHeaderName);
            httpsElem.put(SECURE, true);
            httpsElem.put("id", httpsSessionKey);
            httpsElem.put(NAME, httpsSessionHeaderName);
            arrayHb.put(httpElem);
            arrayHb.put(httpsElem);
            SboxAdapter sbox = SboxAdapter.getInstance();
            sbox.setValue(AuthRequest.SBOX_USER_ID, userId);
            sbox.setValue(AuthRequest.SBOX_AUTH_KEY, authKey);
            sbox.setArrayValues(AuthRequest.SBOX_SESSION, arrayHb);
            sbox.setValue(AuthRequest.SBOX_GRADE_CODE, gradeCode);
            sbox.setValue("cdn_root_url", cdnRoot);
            sbox.setValue("line", line);
            writeToKvs(this, CommonDataBox.PATH_KVS_CONTENT_URI_FOR_AUTH, "line", line);
            writeToKvs(this, CommonDataBox.PATH_KVS_CONTENT_URI_FOR_AUTH, "cdn_root_url", cdnRoot);
            SharedPreferences pref = getSharedPreferences(INIT_PREF_NAME, 0);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(IS_INIT_FLAG, true);
            editor.commit();
            ret = true;
        } catch (Exception e) {
        } finally {
            if (this.sboxConnection != null) {
                if (this.isbox != null) {
                    unbindService(this.sboxConnection);
                    this.isbox = null;
                }
                this.sboxConnection = null;
            }
            finishRequest();
        }
        return ret;
    }

    private void writeToKvs(Context context, Uri uri, String keyName, String value) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CommonDataBox.KVS_DB_COLUMN_NAME_KEY, keyName);
        contentValues.put(CommonDataBox.KVS_DB_COLUMN_NAME_VALUE, value);
        String[] selectionArgs = {keyName};
        try {
            context.getContentResolver().delete(uri, "key = ?", selectionArgs);
            context.getContentResolver().insert(uri, contentValues);
        } catch (Exception e) {
        }
    }

    protected String getPreinSessionKey(ISbox preinSbox, String key, boolean secure) {
        String sessionKey = null;
        try {
            JSONArray sessions = new JSONArray(preinSbox.getArrayValues(key));
            if (sessions != null) {
                for (int i = 0; i < sessions.length(); i++) {
                    JSONObject data = sessions.getJSONObject(i);
                    if (data.has(SECURE) && data.has(NAME) && data.has("id")) {
                        if (data.getString(SECURE).equals(SECURE_TRUE) && secure) {
                            sessionKey = data.getString("id");
                            break;
                        }
                        if (data.getString(SECURE).equals(SECURE_FALSE) && !secure) {
                            sessionKey = data.getString("id");
                            break;
                        }
                    }
                }
            }
            return sessionKey;
        } catch (Exception e) {
            return null;
        }
    }

    protected String getPreinSessionHeaderName(ISbox preinSbox, String key, boolean secure) {
        String headerName = null;
        try {
            JSONArray sessions = new JSONArray(preinSbox.getArrayValues(key));
            if (sessions != null) {
                for (int i = 0; i < sessions.length(); i++) {
                    JSONObject data = sessions.getJSONObject(i);
                    if (data.has(SECURE) && data.has(NAME) && data.has("id")) {
                        if (data.getString(SECURE).equals(SECURE_TRUE) && secure) {
                            headerName = data.getString(NAME);
                            break;
                        }
                        if (data.getString(SECURE).equals(SECURE_FALSE) && !secure) {
                            headerName = data.getString(NAME);
                            break;
                        }
                    }
                }
            }
            return headerName;
        } catch (Exception e) {
            return null;
        }
    }

    void finishRequest() {
        workingFlag = false;
    }

    void releaseWorkingLockTimeout() {
        if (lockCancelTask != null) {
            this.handler.removeCallbacks(lockCancelTask);
            lockCancelTask = null;
        }
    }

    class SessionKeySet {
        IAuthCallback callback;
        boolean secure;

        public SessionKeySet(boolean secure, IAuthCallback callback) {
            this.secure = secure;
            this.callback = callback;
        }
    }

    class AkamaiTokenSet {
        List<String> accessList;
        IAuthCallback callback;
        String url;

        public AkamaiTokenSet(String url, List<String> accessList, IAuthCallback callback) {
            this.url = url;
            this.accessList = accessList;
            this.callback = callback;
        }
    }

    class LockCancelTask implements Runnable {
        LockCancelTask() {
        }

        @Override
        public void run() {
            AuthService.this.finishRequest();
        }
    }
}
