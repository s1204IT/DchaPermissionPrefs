package jp.co.benesse.touch.common.anrsendservice.util;

public class AnalyticsChuzemiUtil extends AnalyticsUtilBase {
    private static final String CLASS_NAME = "jp.co.benesse.touch.asynccomm.GaService";
    public static final String PACKAGE_NAME = "jp.co.benesse.touch.asynccomm";

    @Override
    protected String getClassName() {
        return CLASS_NAME;
    }

    @Override
    protected String getPackageName() {
        return PACKAGE_NAME;
    }
}
