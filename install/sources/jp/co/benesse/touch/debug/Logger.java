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

    public static int d(String tag, Object... messages) {
        Object[] msg = new Object[messages.length + 1];
        msg[0] = tag;
        for (int i = 0; i < messages.length; i++) {
            msg[i + 1] = messages[i];
        }
        return 0;
    }

    public static int d(Context context, Object... messages) {
        Object[] msg = new Object[messages.length + 1];
        msg[0] = context.getPackageName();
        for (int i = 0; i < messages.length; i++) {
            msg[i + 1] = messages[i];
        }
        return 0;
    }

    public static int e(String tag, Object... messages) {
        Object[] msg = new Object[messages.length + 1];
        msg[0] = tag;
        for (int i = 0; i < messages.length; i++) {
            msg[i + 1] = messages[i];
        }
        return write(6, "jp.co.benesse.touch", msg);
    }

    public static int e(Context context, Object... messages) {
        Object[] msg = new Object[messages.length + 1];
        msg[0] = context.getPackageName();
        for (int i = 0; i < messages.length; i++) {
            msg[i + 1] = messages[i];
        }
        return write(6, "jp.co.benesse.touch", msg);
    }

    private static synchronized int write(int priority, String tag, Object... messages) {
        int ret;
        if (VM_NAME.equals(DALVIK)) {
            ret = Log.println(priority, tag, getMessage(messages));
            if (priority > 2) {
                int ret9 = Log.println(2, tag, getMetaInfo());
                ret += ret9;
            }
            Object last = messages[messages.length - 1];
            if (last instanceof Throwable) {
                Throwable tr = (Throwable) last;
                int ret2 = Log.println(6, tag, Log.getStackTraceString(tr));
                ret += ret2;
            }
            if (priority == 6) {
                boolean flg = false;
                int ret3 = 0;
                StackTraceElement[] arr$ = Thread.currentThread().getStackTrace();
                for (StackTraceElement element : arr$) {
                    if (element.getClassName().equals(Logger.class.getName())) {
                        flg = true;
                    } else if (flg) {
                        ret3 += Log.println(priority, tag, "at " + element.toString());
                    }
                }
                ret += ret3;
            }
        } else {
            ret = 0;
        }
        return ret;
    }

    private static String getMessage(Object[] messages) {
        StringBuffer sb = new StringBuffer();
        for (Object message : messages) {
            if (!(message instanceof Throwable)) {
                if (sb.length() > 0) {
                    sb.append(" ");
                }
                sb.append(message);
            }
        }
        return sb.toString();
    }

    private static String getMetaInfo() {
        int index;
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        int len = stackTraceElements.length;
        if (len > 5) {
            index = 5;
        } else {
            index = len - 1;
        }
        boolean flg = false;
        for (int i = 0; i < len; i++) {
            StackTraceElement stackTraceElement = stackTraceElements[i];
            if (stackTraceElement == null || stackTraceElement.getFileName() == null) {
                break;
            }
            if (TextUtils.equals("LogUtils.java", stackTraceElement.getFileName())) {
                flg = true;
            } else if (flg) {
                index = i;
                break;
            }
        }
        StackTraceElement element = stackTraceElements[index];
        StringBuffer sb = new StringBuffer();
        sb.append("    at ");
        sb.append(element.getClassName());
        sb.append(".");
        sb.append(element.getMethodName());
        sb.append("(");
        sb.append(element.getFileName());
        sb.append(":");
        sb.append(element.getLineNumber());
        sb.append(")");
        return sb.toString();
    }
}
