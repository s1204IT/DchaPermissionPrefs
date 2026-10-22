package jp.co.benesse.touch.asynccomm.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;

public class DBProvider extends ContentProvider {
    private static final String AUTHORITY = "jp.co.benesse.touch.asyccomm.provider";
    private static final String DB_NAME_DATA = "AsyncComm.db";
    private static final int DB_VERSION_DATA = 1;
    private static final int IDX_DELETE_SEND_REQUEST = 3;
    private static final int IDX_INSERT_SEND_REQUEST = 1;
    private static final int IDX_SELECT_SEND_REQUEST = 2;
    public static final String PATH_DELETE_SEND_REQUEST = "delete_send_request";
    public static final String PATH_INSERT_SEND_REQUEST = "insert_send_request";
    public static final String PATH_SELECT_SEND_REQUEST = "select_send_request";
    private static final String SORT_ORDER_SELECT_SEND_REQUEST = "Id ASC";
    private static final String TAG = "DBProvider";
    public static final String URI = "content://jp.co.benesse.touch.asyccomm.provider/";
    private static final Object[][] URI_MATCH_DEFINE;
    private static final UriMatcher uriMatcher;
    private DBHelper dbHelper;

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return -1;
    }

    static {
        Object[][] objArr = {new Object[]{PATH_INSERT_SEND_REQUEST, 1}, new Object[]{PATH_SELECT_SEND_REQUEST, 2}, new Object[]{PATH_DELETE_SEND_REQUEST, 3}};
        URI_MATCH_DEFINE = objArr;
        uriMatcher = new UriMatcher(-1);
        for (Object[] objArr2 : objArr) {
            uriMatcher.addURI(AUTHORITY, (String) objArr2[0], ((Integer) objArr2[1]).intValue());
        }
    }

    @Override
    public boolean onCreate() {
        this.dbHelper = new DBHelper(getContext(), DB_NAME_DATA, 1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        try {
            SQLiteDatabase readableDatabase = this.dbHelper.getReadableDatabase();
            if (uriMatcher.match(uri) != 2) {
                return null;
            }
            return readableDatabase.query(false, SendRequestColumns.TABLE, strArr, str, strArr2, null, null, SORT_ORDER_SELECT_SEND_REQUEST, "1");
        } catch (SQLiteException unused) {
            return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) throws Throwable {
        SQLiteDatabase sQLiteDatabase = null;
        long jInsert = -1;
        try {
            SQLiteDatabase writableDatabase = this.dbHelper.getWritableDatabase();
            try {
                writableDatabase.beginTransaction();
                if (uriMatcher.match(uri) == 1) {
                    jInsert = writableDatabase.insert(SendRequestColumns.TABLE, null, contentValues);
                }
                if (jInsert >= 0) {
                    writableDatabase.setTransactionSuccessful();
                }
                if (writableDatabase != null) {
                    writableDatabase.endTransaction();
                }
            } catch (SQLiteException unused) {
                sQLiteDatabase = writableDatabase;
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.endTransaction();
                }
            } catch (Throwable th) {
                th = th;
                sQLiteDatabase = writableDatabase;
                if (sQLiteDatabase != null) {
                    sQLiteDatabase.endTransaction();
                }
                throw th;
            }
        } catch (SQLiteException unused2) {
        } catch (Throwable th2) {
            th = th2;
        }
        return ContentUris.withAppendedId(uri, jInsert);
    }

    @Override
    public int delete(Uri uri, String str, String[] strArr) {
        int iDelete = -1;
        SQLiteDatabase writableDatabase = null;
        try {
            writableDatabase = this.dbHelper.getWritableDatabase();
            writableDatabase.beginTransaction();
            if (uriMatcher.match(uri) == 3) {
                iDelete = writableDatabase.delete(SendRequestColumns.TABLE, str, strArr);
            }
            if (iDelete > 0) {
                writableDatabase.setTransactionSuccessful();
            }
        } catch (SQLiteException unused) {
        } finally {
            if (writableDatabase != null) {
                writableDatabase.endTransaction();
            }
        }
        return iDelete;
    }
}
