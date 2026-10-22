package jp.co.benesse.touch.common.anrsendservice;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import androidx.work.ExistingWorkPolicy;
import androidx.work.ListenableWorker;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import java.util.Map;
import jp.co.benesse.touch.common.anrsendservice.util.Logger;

public class AnrSendProvider extends ContentProvider {
    private static final int EXEC_ID = 1;
    private static final int UNSPECIFIED_ID = 0;
    private static UriMatcher sUriMatcher;
    protected WorkManager mWorkManager;
    public static final String AUTHORITY = AnrSendProvider.class.getPackage().getName();
    private static final String TAG = AnrSendProvider.class.getSimpleName();

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        sUriMatcher = uriMatcher;
        uriMatcher.addURI(AUTHORITY, "/", 0);
        sUriMatcher.addURI(AUTHORITY, "exec", 1);
    }

    @Override
    public boolean onCreate() {
        Logger.d(TAG, "onCreate 001");
        this.mWorkManager = WorkManager.getInstance(getContext());
        Logger.d(TAG, "onCreate 002");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        Logger.d(TAG, "query 001");
        return null;
    }

    @Override
    public String getType(Uri uri) {
        Logger.d(TAG, "getType 001");
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        Logger.d(TAG, "insert 001");
        return null;
    }

    @Override
    public int delete(Uri uri, String str, String[] strArr) {
        Logger.d(TAG, "delete 001");
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        int i;
        Logger.d(TAG, "update 001");
        if (sUriMatcher.match(uri) == 1) {
            Logger.d(TAG, "update 002");
            for (Map.Entry<String, Object> entry : contentValues.valueSet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                if (value instanceof String) {
                    Logger.d(TAG, "key:", key, "value:", value);
                }
            }
            this.mWorkManager.beginUniqueWork(AnrSendWorker.class.getSimpleName(), ExistingWorkPolicy.REPLACE, OneTimeWorkRequest.from((Class<? extends ListenableWorker>) AnrSendWorker.class)).enqueue();
            i = 1;
        } else {
            Logger.d(TAG, "update 003");
            i = 0;
        }
        Logger.d(TAG, "update 004");
        return i;
    }
}
