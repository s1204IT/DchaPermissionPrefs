package jp.co.benesse.touch.debug;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public final class Logger {
    private static final String DALVIK = "Dalvik";
    private static final String TAG = "jp.co.benesse.touch";
    private static final int TARGET_INDEX = 5;
    private static final String VM_NAME = System.getProperty("java.vm.name");

    private Logger() {
    }

    public static int d(String str, Object... objArr) {
        Object[] objArr2 = new Object[objArr.length + 1];
        objArr2[0] = str;
        int i = 0;
        while (i < objArr.length) {
            int i2 = i + 1;
            objArr2[i2] = objArr[i];
            i = i2;
        }
        return 0;
    }

    public static int d(Context context, Object... objArr) {
        Object[] objArr2 = new Object[objArr.length + 1];
        objArr2[0] = context.getPackageName();
        int i = 0;
        while (i < objArr.length) {
            int i2 = i + 1;
            objArr2[i2] = objArr[i];
            i = i2;
        }
        return 0;
    }

    public static int e(String str, Object... objArr) {
        Object[] objArr2 = new Object[objArr.length + 1];
        int i = 0;
        objArr2[0] = str;
        while (i < objArr.length) {
            int i2 = i + 1;
            objArr2[i2] = objArr[i];
            i = i2;
        }
        return write(6, "jp.co.benesse.touch", objArr2);
    }

    public static int e(Context context, Object... objArr) {
        Object[] objArr2 = new Object[objArr.length + 1];
        int i = 0;
        objArr2[0] = context.getPackageName();
        while (i < objArr.length) {
            int i2 = i + 1;
            objArr2[i2] = objArr[i];
            i = i2;
        }
        return write(6, "jp.co.benesse.touch", objArr2);
    }

    private static synchronized int write(int i, String str, Object... objArr) {
        if (!VM_NAME.equals(DALVIK)) {
            return 0;
        }
        int iPrintln = Log.println(i, str, getMessage(objArr));
        if (i > 2) {
            iPrintln += Log.println(2, str, getMetaInfo());
        }
        Object obj = objArr[objArr.length - 1];
        if (obj instanceof Throwable) {
            iPrintln += Log.println(6, str, Log.getStackTraceString((Throwable) obj));
        }
        if (i == 6) {
            int iPrintln2 = 0;
            boolean z = false;
            for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
                if (stackTraceElement.getClassName().equals(Logger.class.getName())) {
                    z = true;
                } else if (z) {
                    iPrintln2 += Log.println(i, str, "at " + stackTraceElement.toString());
                }
            }
            iPrintln += iPrintln2;
        }
        return iPrintln;
    }

    private static String getMessage(Object[] objArr) {
        StringBuffer stringBuffer = new StringBuffer();
        for (Object obj : objArr) {
            if (!(obj instanceof Throwable)) {
                if (stringBuffer.length() > 0) {
                    stringBuffer.append(" ");
                }
                stringBuffer.append(obj);
            }
        }
        return stringBuffer.toString();
    }

    private static String getMetaInfo() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int length = stackTrace.length;
        int i = length <= 5 ? length - 1 : 5;
        boolean z = false;
        for (int i2 = 0; i2 < length; i2++) {
            StackTraceElement stackTraceElement = stackTrace[i2];
            if (stackTraceElement == null || stackTraceElement.getFileName() == null) {
                break;
            }
            if (!TextUtils.equals("LogUtils.java", stackTraceElement.getFileName())) {
                if (z) {
                    i = i2;
                    break;
                }
            } else {
                z = true;
            }
        }
        StackTraceElement stackTraceElement2 = stackTrace[i];
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("    at ");
        stringBuffer.append(stackTraceElement2.getClassName());
        stringBuffer.append(".");
        stringBuffer.append(stackTraceElement2.getMethodName());
        stringBuffer.append("(");
        stringBuffer.append(stackTraceElement2.getFileName());
        stringBuffer.append(":");
        stringBuffer.append(stackTraceElement2.getLineNumber());
        stringBuffer.append(")");
        return stringBuffer.toString();
    }
}
