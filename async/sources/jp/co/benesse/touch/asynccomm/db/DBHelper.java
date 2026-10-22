package jp.co.benesse.touch.asynccomm.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

public class DBHelper extends SQLiteOpenHelper {
    private static final String INIT_SQL_FILE_PATH = "sql/init.sql";
    private static final String REGEX_SQL_COMMENT = "\\s--.*$";
    private static final String SQL_DIR_PATH = "sql/";
    private static final String SQL_SEPARATOR = ";";
    private static final String TAG = "DBHelper";
    private Context context;

    @Override
    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
    }

    public DBHelper(Context context, String str, int i) {
        super(context, str, (SQLiteDatabase.CursorFactory) null, i);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        try {
            execAssetsSql(sQLiteDatabase, INIT_SQL_FILE_PATH);
        } catch (SQLException | IOException unused) {
        }
    }

    private void execAssetsSql(SQLiteDatabase sQLiteDatabase, String str) throws Throwable {
        InputStream inputStreamOpen;
        StringBuilder sb = new StringBuilder();
        Pattern patternCompile = Pattern.compile(REGEX_SQL_COMMENT);
        BufferedReader bufferedReader = null;
        try {
            inputStreamOpen = this.context.getAssets().open(str);
            try {
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStreamOpen));
                try {
                    sQLiteDatabase.beginTransaction();
                    while (true) {
                        String line = bufferedReader2.readLine();
                        if (line == null) {
                            break;
                        }
                        String strReplaceAll = patternCompile.matcher(line).replaceAll("");
                        if (strReplaceAll.trim().length() > 0) {
                            sb.append(strReplaceAll);
                            sb.append("\n");
                        }
                    }
                    String[] strArrSplit = sb.toString().split(SQL_SEPARATOR);
                    for (int i = 0; i < strArrSplit.length - 1; i++) {
                        sQLiteDatabase.execSQL(strArrSplit[i]);
                    }
                    sQLiteDatabase.setTransactionSuccessful();
                    try {
                        bufferedReader2.close();
                    } catch (IOException unused) {
                    }
                    if (inputStreamOpen != null) {
                        try {
                            inputStreamOpen.close();
                        } catch (IOException unused2) {
                        }
                    }
                    sQLiteDatabase.endTransaction();
                } catch (Throwable th) {
                    th = th;
                    bufferedReader = bufferedReader2;
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException unused3) {
                        }
                    }
                    if (inputStreamOpen != null) {
                        try {
                            inputStreamOpen.close();
                        } catch (IOException unused4) {
                        }
                    }
                    sQLiteDatabase.endTransaction();
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (Throwable th3) {
            th = th3;
            inputStreamOpen = null;
        }
    }
}
