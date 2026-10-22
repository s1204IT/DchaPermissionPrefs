package jp.co.benesse.touch.asynccomm;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import jp.co.benesse.touch.asynccomm.db.SendRequestColumns;
import jp.co.benesse.touch.asynccomm.util.NetStateUtil;
import jp.co.benesse.touch.auth.IAuthService;
import jp.co.benesse.touch.common.touchconstant.AuthRequest;
import jp.co.benesse.touch.common.touchconstant.IntentName;

public class AsyncCommLogic {
    private static final String PREFIX_REPLACE = "@@@";
    public static final String[] REPLACE_KEYWORDS_FOR_REGIST = {AuthRequest.SBOX_GRADE_CODE};
    public static final String[] REPLACE_KEYWORDS_FOR_SEND = {AuthRequest.SBOX_USER_ID, AuthRequest.SBOX_GRADE_CODE, "line"};
    private static final int RESULT_ILLEGAL_STATUS_ERROR = -1;
    private static final int RESULT_UNEXPECTED_ERROR = -2;
    private static final String TAG = "AsyncCommLogic";
    private AsyncCommService service;

    public AsyncCommLogic(AsyncCommService asyncCommService) {
        this.service = asyncCommService;
    }

    public void createSendRequest(Intent intent) {
        SendRequestDto sendRequestDto = null;
        if (intent != null) {
            try {
                if (intent.hasExtra("url") && intent.hasExtra(IntentName.EXTRA_ASYNCCOMM_REQUEST)) {
                    String stringExtra = intent.getStringExtra(IntentName.EXTRA_ASYNCCOMM_REQUEST);
                    boolean booleanExtra = intent.getBooleanExtra(IntentName.EXTRA_ASYNCCOMM_REPLACE_FLAG, false);
                    if (booleanExtra) {
                        stringExtra = replaceRequest(stringExtra, REPLACE_KEYWORDS_FOR_REGIST);
                    }
                    sendRequestDto = new SendRequestDto(intent.getStringExtra("url"), intent.getStringExtra(IntentName.EXTRA_ASYNCCOMM_API), stringExtra, booleanExtra);
                }
            } catch (RemoteException unused) {
            }
        }
        if (sendRequestDto != null) {
            registSendRequest(sendRequestDto);
        }
    }

    private void registSendRequest(SendRequestDto sendRequestDto) {
        try {
            Uri uri = Uri.parse("content://jp.co.benesse.touch.asyccomm.provider/insert_send_request");
            ContentValues contentValues = new ContentValues();
            contentValues.put(SendRequestColumns.URL, sendRequestDto.getUrl());
            contentValues.put(SendRequestColumns.API, sendRequestDto.getApi());
            contentValues.put("Request", sendRequestDto.getRequest());
            contentValues.put(SendRequestColumns.REPLACE_FLAG, Boolean.valueOf(sendRequestDto.isReplaceFlag()));
            this.service.getContentResolver().insert(uri, contentValues);
        } catch (NullPointerException unused) {
        }
    }

    private SendRequestDto getSendRequest() {
        String strReplaceRequest;
        int i;
        try {
            Cursor cursorQuery = this.service.getContentResolver().query(Uri.parse("content://jp.co.benesse.touch.asyccomm.provider/select_send_request"), SendRequestColumns.PROJECTION_SELECT_SEND_REQUEST, null, null, null);
            if (cursorQuery == null) {
                return null;
            }
            int count = cursorQuery.getCount();
            if (count != 1) {
                if (count == 0) {
                }
                return null;
            }
            cursorQuery.moveToNext();
            String string = cursorQuery.getString(cursorQuery.getColumnIndex("Request"));
            int i2 = cursorQuery.getInt(cursorQuery.getColumnIndex(SendRequestColumns.REPLACE_FLAG));
            if (i2 > 0) {
                strReplaceRequest = replaceRequest(string, REPLACE_KEYWORDS_FOR_SEND);
                i = 0;
            } else {
                strReplaceRequest = string;
                i = i2;
            }
            SendRequestDto sendRequestDto = new SendRequestDto(cursorQuery.getInt(cursorQuery.getColumnIndex(SendRequestColumns.ID)), cursorQuery.getString(cursorQuery.getColumnIndex(SendRequestColumns.URL)), cursorQuery.getString(cursorQuery.getColumnIndex(SendRequestColumns.API)), strReplaceRequest, i);
            cursorQuery.close();
            return sendRequestDto;
        } catch (RemoteException | NullPointerException unused) {
            return null;
        }
    }

    public int executeSendRequest() {
        SendRequestDto sendRequest;
        boolean zAwait;
        int i = 0;
        while (NetStateUtil.checkNetState(this.service.getApplicationContext()) && (sendRequest = getSendRequest()) != null) {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            HttpResponse httpResponse = new HttpResponse(countDownLatch);
            HttpRequestTask httpRequestTask = new HttpRequestTask(this.service, httpResponse);
            httpRequestTask.execute(sendRequest);
            try {
                zAwait = countDownLatch.await(30000L, TimeUnit.MILLISECONDS);
            } catch (InterruptedException unused) {
                zAwait = false;
            }
            if (!zAwait) {
                httpRequestTask.cancel(false);
            }
            if (httpResponse.getRetryFlag()) {
                break;
            }
            deleteSendRequest(sendRequest);
            i++;
        }
        return i;
    }

    private int deleteSendRequest(SendRequestDto sendRequestDto) {
        try {
            if (sendRequestDto.getId() != null) {
                return this.service.getContentResolver().delete(Uri.parse("content://jp.co.benesse.touch.asyccomm.provider/delete_send_request"), "Id = ?", new String[]{String.valueOf(sendRequestDto.getId())});
            }
            return -1;
        } catch (NullPointerException unused) {
            return -2;
        }
    }

    private String replaceRequest(String str, String[] strArr) throws RemoteException {
        String str2 = new String(str);
        IAuthService authService = this.service.getAuthService();
        for (int i = 0; i < strArr.length; i++) {
            str2 = Pattern.compile(getReplaceKeyWord(strArr[i])).matcher(str2).replaceAll(authService.getStringValue(strArr[i]));
        }
        return str2;
    }

    private String getReplaceKeyWord(String str) {
        return PREFIX_REPLACE + str + PREFIX_REPLACE;
    }

    class HttpResponse implements IHttpResponse {
        private static final String TAG_IHTTP_RESPONSE = "HttpResponse";
        private CountDownLatch signal;
        private boolean retryFlag = false;
        private int resultCode = 1001;
        private int statusCode = 0;
        private String response = null;

        HttpResponse(CountDownLatch countDownLatch) {
            this.signal = countDownLatch;
        }

        public boolean getRetryFlag() {
            return this.retryFlag;
        }

        public int getResultCode() {
            return this.resultCode;
        }

        @Override
        public void setStatusCode(int i) {
            this.statusCode = i;
        }

        @Override
        public int getStatusCode() {
            return this.statusCode;
        }

        @Override
        public void setResponse(String str) {
            this.response = str;
        }

        @Override
        public String getResponse() {
            return this.response;
        }

        @Override
        public void onRespose(int i) {
            this.resultCode = i;
            if (!Arrays.asList(IHttpResponse.NO_RETRY_LIST).contains(Integer.valueOf(this.resultCode))) {
                this.retryFlag = true;
            }
            this.signal.countDown();
        }
    }
}
