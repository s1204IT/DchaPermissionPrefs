package jp.co.benesse.dcha.util;

public final class MathUtils {
    private MathUtils() {
    }

    public static final int sum(int[] elements) {
        int result = 0;
        if (elements == null) {
            return 0;
        }
        for (int element : elements) {
            result += element;
        }
        return result;
    }

    public static final long sum(long[] elements) {
        long result = 0;
        if (elements == null) {
            return 0L;
        }
        for (long element : elements) {
            result += element;
        }
        return result;
    }
}
