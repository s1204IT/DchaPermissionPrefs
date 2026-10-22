package jp.co.benesse.dcha.util;

import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public final class ViewUtils {
    public static final int BOLD = 1;
    public static final int BOLDITALIC = 3;
    public static final String FONT_FAMILY_NAME = "gjsgm";
    public static final int ITALIC = 2;

    private ViewUtils() {
    }

    public static void setFontAllView(View view) {
        Typeface tf = Typeface.create(FONT_FAMILY_NAME, 0);
        set(view, tf);
    }

    public static void setFontStyle(View view, int style) {
        if (style >= 1 || style <= 3) {
            Typeface tf = Typeface.create(FONT_FAMILY_NAME, 0);
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(tf, style);
            }
        }
    }

    private static void set(View view, Typeface tf) {
        if (tf != null) {
            if (view instanceof ViewGroup) {
                int childCnt = ((ViewGroup) view).getChildCount();
                if (childCnt > 0) {
                    for (int i = 0; i < childCnt; i++) {
                        set(((ViewGroup) view).getChildAt(i), tf);
                    }
                    return;
                }
                return;
            }
            if (view instanceof TextView) {
                ((TextView) view).setTypeface(tf);
            }
        }
    }
}
