package jp.co.benesse.touch.auth;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import java.lang.ref.WeakReference;
import org.json.JSONArray;
import org.json.JSONException;

public class SboxAdapter {
    private static final String APP_IDENTIFIER = "/W;>Fr,11wc[NC4c`+PrKqyO*_]jcI(>";
    private static final String SBOX_DB_COLUMN_NAME_KEY = "key";
    private static final String SBOX_DB_COLUMN_NAME_VALUE = "value";
    private static final Uri SBOX_URI = Uri.parse("content://jp.co.benesse.touch.sbox/jp.co.benesse.touch.auth");
    public static final String TAG = "SboxAdapter";
    private static SboxAdapter instance;
    private final Sbox sboxLib;
    private final WeakReference<Context> wrContext;

    private SboxAdapter(Context context) {
        this.wrContext = new WeakReference<>(context);
        this.sboxLib = existsSboxProvider(context.getContentResolver()) ? null : Sbox.getInstance();
    }

    public static synchronized SboxAdapter createInstance(Context context) {
        if (instance == null) {
            instance = new SboxAdapter(context);
        }
        return instance;
    }

    public static synchronized SboxAdapter getInstance() {
        return instance;
    }

    public synchronized String getValue(String key) {
        String ret;
        try {
            if (key == null) {
                throw new IllegalArgumentException();
            }
            Context context = this.wrContext.get();
            if (context == null) {
                throw new IllegalStateException();
            }
            if (this.sboxLib != null) {
                ret = this.sboxLib.getValue(key);
            } else {
                ret = getSboxProvider(context.getContentResolver(), key);
            }
        } catch (Throwable th) {
            throw th;
        }
        return ret;
    }

    public synchronized void setValue(String key, String value) {
        try {
            if (key == null && value == null) {
                throw new IllegalArgumentException();
            }
            Context context = this.wrContext.get();
            if (context == null) {
                throw new IllegalStateException();
            }
            if (this.sboxLib != null) {
                this.sboxLib.setValue(key, value);
            } else {
                setSboxProvider(context.getContentResolver(), key, value);
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    public synchronized JSONArray getArrayValues(String key) {
        JSONArray ret;
        try {
            if (key == null) {
                throw new IllegalArgumentException();
            }
            Context context = this.wrContext.get();
            if (context == null) {
                throw new IllegalStateException();
            }
            ret = null;
            if (this.sboxLib != null) {
                ret = this.sboxLib.getArrayValues(key);
            } else {
                String values = getSboxProvider(context.getContentResolver(), key);
                try {
                    JSONArray ret2 = new JSONArray(values);
                    ret = ret2;
                } catch (JSONException e) {
                }
            }
        } catch (Throwable th) {
            throw th;
        }
        return ret;
    }

    public synchronized void setArrayValues(String key, JSONArray values) {
        try {
            if (key == null && values == null) {
                throw new IllegalArgumentException();
            }
            Context context = this.wrContext.get();
            if (context == null) {
                throw new IllegalStateException();
            }
            if (this.sboxLib != null) {
                this.sboxLib.setArrayValues(key, values);
            } else {
                setSboxProvider(context.getContentResolver(), key, values.toString());
            }
        } catch (Throwable th) {
            throw th;
        }
    }

    private static boolean existsSboxProvider(ContentResolver cr) {
        try {
            return !TextUtils.isEmpty(cr.getType(SBOX_URI));
        } catch (Exception e) {
            return false;
        }
    }

    protected String getSboxProvider(ContentResolver cr, String key) {
        String ret = null;
        Cursor cursor = null;
        try {
            String[] selectionArgs = {key};
            cursor = cr.query(SBOX_URI, null, "key = ?", selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                ret = cursor.getString(cursor.getColumnIndex("value"));
            }
        } catch (Exception e) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return ret;
    }

    protected void setSboxProvider(ContentResolver cr, String key, String value) {
        try {
            ContentValues cv = new ContentValues();
            cv.put("key", key);
            cv.put("value", value);
            String[] selectionArgs = {key};
            cr.delete(SBOX_URI, "key = ?", selectionArgs);
            cr.insert(SBOX_URI, cv);
        } catch (Exception e) {
        }
    }

    public static String getAppIdentifier() {
        return APP_IDENTIFIER;
    }
}
