package jp.co.benesse.touch.comm;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import java.io.File;
import java.lang.ref.WeakReference;
import jp.co.benesse.dcha.dchaservice.IDchaService;
import jp.co.benesse.touch.auth.IAuthService;
import jp.co.benesse.touch.common.touchconstant.CommResponse;
import jp.co.benesse.touch.common.touchconstant.CommonConst;
import jp.co.benesse.touch.common.touchconstant.IntentName;

@SuppressLint({"NewApi"})
public class CommService extends Service {
    private static String COMM_HANDLER_NAME = "CommServiceHandler";
    private static final String DEFAULT_CACHE_DIR = "volley";
    public static final String ERROR_MESSAGE = "errormessage";
    public static final String HTTPS_SESSIONKEY_HEADER_PARAM = "https_sessionkey_header";
    public static final int HTTP_CONNECTION_RETRY_COUNT = 2;
    public static final int HTTP_CONNECTION_TIMEOUT_MS = 30000;
    public static final String HTTP_SESSIONKEY_HEADER_PARAM = "http_sessionkey_header";
    private static final int MAX_REQUEST_THREAD_POOL_NUM = 8;
    public static final String PROGRESS_RATE = "progressrate";
    protected static final int REQ_AUTH_SERVER = 1;
    protected static final int REQ_BAE_SERVER = 0;
    protected static final int REQ_HTTP_SERVER = 3;
    protected static final int REQ_MEMBER_SERVER = 2;
    protected static final int REQ_SET_SERVER_TIME = 4;
    public static final long RESPONSE_WAIT_TIMEOUT_MS = 100000;
    public static final String TAG = "CommService";
    private IAuthService authService;
    private IDchaService dchaService;
    private Handler handler;
    RequestQueue httpReqQueue;
    boolean isAuthServiceDisconnected = false;
    boolean isDchaServiceDisconnected = false;
    public boolean cancelFlag = false;
    HandlerThread handlerThread = null;
    private ServiceConnection authServiceConnection = new AuthServiceConnection();
    private ServiceConnection dchaServiceConnection = new DchaServiceConnection();
    private ICommService.Stub mCommServiceStub = new ICommService.Stub() {
        @Override
        public void requestComm2BaeServer(IHttpRequest req, IHttpResponse res) throws RemoteException {
            CommService.this.cancelFlag = false;
            Message msg = CommService.this.handler.obtainMessage();
            msg.what = 0;
            HttpReqResSet set = CommService.this.new HttpReqResSet(req, res);
            msg.obj = set;
            CommService.this.handler.sendMessage(msg);
        }

        @Override
        public void requestComm2AuthServer(IHttpRequest req, IHttpResponse res) {
            CommService.this.cancelFlag = false;
            Message msg = CommService.this.handler.obtainMessage();
            msg.what = 1;
            HttpReqResSet set = CommService.this.new HttpReqResSet(req, res);
            msg.obj = set;
            CommService.this.handler.sendMessageAtFrontOfQueue(msg);
        }

        @Override
        public void requestComm2MemberServer(IHttpRequest req, IHttpResponse res) {
            CommService.this.cancelFlag = false;
            Message msg = CommService.this.handler.obtainMessage();
            msg.what = 2;
            HttpReqResSet set = CommService.this.new HttpReqResSet(req, res);
            msg.obj = set;
            CommService.this.handler.sendMessageAtFrontOfQueue(msg);
        }

        @Override
        public void requestComm2HttpServer(IHttpRequest req, IHttpResponse res) throws RemoteException {
            CommService.this.cancelFlag = false;
            Message msg = CommService.this.handler.obtainMessage();
            msg.what = 3;
            HttpReqResSet set = CommService.this.new HttpReqResSet(req, res);
            msg.obj = set;
            CommService.this.handler.sendMessage(msg);
        }

        @Override
        public void setServerTime() throws RemoteException {
            Message msg = CommService.this.handler.obtainMessage();
            msg.what = 4;
            CommService.this.handler.sendMessage(msg);
        }
    };
    private final BroadcastReceiver shutdownReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.ACTION_SHUTDOWN")) {
                CommService.this.cancelAllRequest();
            }
        }
    };

    class AuthServiceConnection implements ServiceConnection {
        AuthServiceConnection() {
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            CommService.this.authService = IAuthService.Stub.asInterface(service);
            CommService.this.isAuthServiceDisconnected = false;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            CommService.this.authService = null;
            CommService.this.isAuthServiceDisconnected = true;
        }
    }

    class DchaServiceConnection implements ServiceConnection {
        DchaServiceConnection() {
        }

        @Override
        public void onServiceConnected(ComponentName arg0, IBinder service) {
            CommService.this.dchaService = IDchaService.Stub.asInterface(service);
            CommService.this.isDchaServiceDisconnected = false;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            CommService.this.dchaService = null;
            CommService.this.isDchaServiceDisconnected = true;
        }
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return this.mCommServiceStub;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createRequestQueue();
        this.httpReqQueue.start();
        if (this.handlerThread == null) {
            this.handlerThread = new HandlerThread(COMM_HANDLER_NAME, 10);
            this.handlerThread.start();
            Looper looper = this.handlerThread.getLooper();
            this.handler = new HandlerExtension(looper, this);
        }
        IntentFilter shutdownFilter = new IntentFilter();
        shutdownFilter.addAction("android.intent.action.ACTION_SHUTDOWN");
        registerReceiver(this.shutdownReceiver, shutdownFilter);
        Intent dchaIntent = new Intent(IntentName.ACTION_DCHA_SERVICE);
        dchaIntent.setPackage(IntentName.PKG_DCHA_SERVICE);
        bindService(dchaIntent, this.dchaServiceConnection, 1);
        Intent authIntent = new Intent(IntentName.ACTION_AUTH_SERVICE);
        authIntent.setPackage(IntentName.PKG_AUTH_SERVICE);
        bindService(authIntent, this.authServiceConnection, 1);
    }

    private void createRequestQueue() {
        Context context = getBaseContext();
        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);
        HttpStack stack = new HurlStack();
        Network network = new BasicNetwork(stack);
        this.httpReqQueue = new RequestQueue(new DiskBasedCache(cacheDir), network, MAX_REQUEST_THREAD_POOL_NUM);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.httpReqQueue.stop();
        if (this.authServiceConnection != null) {
            if (this.authService != null) {
                unbindService(this.authServiceConnection);
                this.authService = null;
            }
            this.authServiceConnection = null;
        }
        if (this.dchaServiceConnection != null) {
            if (this.dchaService != null) {
                unbindService(this.dchaServiceConnection);
                this.dchaService = null;
            }
            this.dchaServiceConnection = null;
        }
        if (this.shutdownReceiver != null) {
            unregisterReceiver(this.shutdownReceiver);
        }
    }

    protected IAuthService getAuthService() {
        if (this.authService == null) {
            if (this.isAuthServiceDisconnected) {
                this.authServiceConnection = new AuthServiceConnection();
                Intent intent = new Intent(IntentName.ACTION_AUTH_SERVICE);
                intent.setPackage(IntentName.PKG_AUTH_SERVICE);
                bindService(intent, this.authServiceConnection, 1);
            }
            for (int count = 0; count < 50 && this.authService == null; count++) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                }
            }
        }
        return this.authService;
    }

    protected IDchaService getDchaService() {
        if (this.dchaService == null) {
            if (this.isDchaServiceDisconnected) {
                this.dchaServiceConnection = new DchaServiceConnection();
                Intent intent = new Intent(IntentName.ACTION_DCHA_SERVICE);
                intent.setPackage(IntentName.PKG_DCHA_SERVICE);
                bindService(intent, this.dchaServiceConnection, 1);
            }
            for (int count = 0; count < 50 && this.dchaService == null; count++) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                }
            }
        }
        return this.dchaService;
    }

    void requestBaeServer(IHttpRequest req, IHttpResponse res, boolean retry) {
        BaeRequestTask baeRequestTask = new BaeRequestTask(req, res, this, retry);
        Thread baeRequestThread = new Thread(baeRequestTask);
        baeRequestThread.start();
    }

    private void requestAuthServer(IHttpRequest req, IHttpResponse res) {
        AuthRequestTask authRequestTask = new AuthRequestTask(req, res, this);
        Thread authRequestThread = new Thread(authRequestTask);
        authRequestThread.start();
    }

    void requestMemberServer(IHttpRequest req, IHttpResponse res, boolean retry) {
        MemberRequestTask memberRequestTask = new MemberRequestTask(req, res, this, retry);
        Thread memberRequestThread = new Thread(memberRequestTask);
        memberRequestThread.start();
    }

    void requestHttpServer(IHttpRequest req, IHttpResponse res) {
        HttpRequestTask httpRequestTask = new HttpRequestTask(req, res, this);
        Thread httpRequestThread = new Thread(httpRequestTask);
        httpRequestThread.start();
    }

    protected void cancelAllRequest() {
        this.cancelFlag = true;
        this.httpReqQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    protected void onResult(String reqID, String uri, int resultCode, String msg) {
        Intent resultIntent = new Intent();
        resultIntent.setAction(IntentName.ACTION_COMM_SERVICE_RESPONSE);
        resultIntent.addCategory(IntentName.CATEGORY_COMM_SERVICE_RESULT_RESPONSE);
        resultIntent.putExtra(CommResponse.RES_COMM_KEY_REQUEST_ID, reqID);
        resultIntent.putExtra(CommResponse.RES_COMM_KEY_REQUEST_URI, uri);
        resultIntent.putExtra("resultcode", resultCode);
        resultIntent.putExtra(CommResponse.RES_COMM_KEY_RESULT_MESSAGE, msg);
        sendBroadcast(resultIntent);
    }

    protected void onError(String reqID, String uri, int errCode, String errMsg, int statusCode) {
        Intent errorIntent = new Intent();
        errorIntent.setAction(IntentName.ACTION_COMM_SERVICE_RESPONSE);
        errorIntent.addCategory(IntentName.CATEGORY_COMM_SERVICE_RESULT_RESPONSE);
        errorIntent.putExtra(CommResponse.RES_COMM_KEY_REQUEST_ID, reqID);
        errorIntent.putExtra(CommResponse.RES_COMM_KEY_REQUEST_URI, uri);
        errorIntent.putExtra("resultcode", errCode);
        errorIntent.putExtra(CommResponse.RES_COMM_KEY_RESULT_MESSAGE, errMsg);
        errorIntent.putExtra(CommResponse.RES_COMM_KEY_ERR_STATUS_CODE, statusCode);
        sendBroadcast(errorIntent);
    }

    class HttpReqResSet {
        IHttpRequest req;
        IHttpResponse res;

        public HttpReqResSet(IHttpRequest req, IHttpResponse res) {
            this.req = null;
            this.res = null;
            this.req = req;
            this.res = res;
        }
    }

    class ServerTimeListener {
        boolean isSetTime;
        public String sttCd = "";

        public ServerTimeListener(boolean b) {
            this.isSetTime = true;
            this.isSetTime = b;
        }

        void setServerTime(String time) {
            try {
                if (this.isSetTime) {
                    CommService.this.dchaService.setSystemTime(time, CommonConst.FORMAT_DATE_YYYYMMDDHHMMSS);
                }
            } catch (RemoteException e) {
            }
        }
    }

    static class HandlerExtension extends Handler {
        private final WeakReference<CommService> service;

        HandlerExtension(Looper looper, CommService service) {
            super(looper);
            this.service = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            CommService commService;
            if (this.service != null && (commService = this.service.get()) != null) {
                switch (msg.what) {
                    case 0:
                        if (msg.obj instanceof HttpReqResSet) {
                            HttpReqResSet set = (HttpReqResSet) msg.obj;
                            commService.requestBaeServer(set.req, set.res, true);
                        }
                        break;
                    case 1:
                        if (msg.obj instanceof HttpReqResSet) {
                            HttpReqResSet set2 = (HttpReqResSet) msg.obj;
                            commService.requestAuthServer(set2.req, set2.res);
                        }
                        break;
                    case 2:
                        if (msg.obj instanceof HttpReqResSet) {
                            HttpReqResSet set3 = (HttpReqResSet) msg.obj;
                            commService.requestMemberServer(set3.req, set3.res, true);
                        }
                        break;
                    case 3:
                        if (msg.obj instanceof HttpReqResSet) {
                            HttpReqResSet set4 = (HttpReqResSet) msg.obj;
                            commService.requestHttpServer(set4.req, set4.res);
                        }
                        break;
                    case 4:
                        if (this.service != null) {
                            SettingServerTimeThread settingServerTimeThread = new SettingServerTimeThread(commService);
                            new Thread(settingServerTimeThread).start();
                        }
                        break;
                }
            }
        }
    }
}
