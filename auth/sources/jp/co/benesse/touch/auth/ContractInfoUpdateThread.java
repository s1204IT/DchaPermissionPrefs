package jp.co.benesse.touch.auth;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.text.TextUtils;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jp.co.benesse.touch.comm.ICommService;
import jp.co.benesse.touch.common.touchconstant.CommonDataBox;

public class ContractInfoUpdateThread implements Runnable {
    public static final String TAG = "CookieGetThread";
    private AuthService authService;
    private IAuthCallback callback;
    Lock responseWaitLock = new ReentrantLock();
    Condition responseWaitCond = this.responseWaitLock.newCondition();
    ContractInfoWaitingCheckListener waitResponseFlagListener = new ContractInfoWaitingCheckListener(false);

    public ContractInfoUpdateThread(AuthService authService, IAuthCallback callback) {
        this.authService = authService;
        this.callback = callback;
    }

    @Override
    public void run() {
        String result = null;
        try {
            Context context = this.authService.getApplicationContext();
            String sboxLine = SboxAdapter.getInstance().getValue("line");
            String kvsLine = readToKvs(context, CommonDataBox.PATH_KVS_CONTENT_URI_FOR_AUTH, "line");
            String line = TextUtils.isEmpty(sboxLine) ? kvsLine : sboxLine;
            if (TextUtils.isEmpty(sboxLine)) {
                SboxAdapter.getInstance().setValue("line", kvsLine);
            }
            if (TextUtils.isEmpty(kvsLine)) {
                writeToKvs(context, CommonDataBox.PATH_KVS_CONTENT_URI_FOR_AUTH, "line", sboxLine);
            }
            ContractInfoRequest req = new ContractInfoRequest(context);
            ContractInfoResponse res = new ContractInfoResponse(line, this.responseWaitLock, this.responseWaitCond, this.waitResponseFlagListener);
            this.responseWaitLock.lock();
            ICommService commService = this.authService.getCommService();
            commService.requestComm2MemberServer(req, res);
            long timeout = 100000000000L;
            while (!this.waitResponseFlagListener.getFlag()) {
                long l = this.responseWaitCond.awaitNanos(timeout);
                if (l <= 0 || this.waitResponseFlagListener.getFlag()) {
                    break;
                } else {
                    timeout = l;
                }
            }
            this.waitResponseFlagListener.setFlag(false);
            String result2 = null;
            if (res.sttCd != null && res.sttCd.equals(AuthService.COMPLETE_NORMAL)) {
                result2 = res.zemiKubun;
            }
            this.responseWaitLock.unlock();
            try {
                if (this.callback != null) {
                }
            } catch (RemoteException e) {
            }
        } catch (Exception e2) {
            this.responseWaitLock.unlock();
            try {
                if (this.callback != null) {
                }
            } catch (RemoteException e3) {
            }
        } finally {
            this.responseWaitLock.unlock();
            try {
                if (this.callback != null) {
                    this.callback.setStringValue(result);
                    this.callback.onResult();
                }
            } catch (RemoteException e4) {
            }
            this.authService.finishRequest();
        }
    }

    private String readToKvs(Context context, Uri uri, String keyName) {
        String result = null;
        Cursor cursor = null;
        try {
            String[] selectionArgs = {keyName};
            cursor = context.getContentResolver().query(uri, null, "key = ?", selectionArgs, null);
            if (cursor != null && cursor.moveToNext()) {
                result = cursor.getString(2);
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
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

    class ContractInfoWaitingCheckListener {
        boolean flag;

        public ContractInfoWaitingCheckListener(boolean b) {
            this.flag = false;
            this.flag = b;
        }

        public boolean getFlag() {
            return this.flag;
        }

        public void setFlag(boolean b) {
            this.flag = b;
        }
    }
}
