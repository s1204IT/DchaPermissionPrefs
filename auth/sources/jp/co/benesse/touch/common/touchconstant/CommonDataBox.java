package jp.co.benesse.touch.common.touchconstant;

import android.net.Uri;

public final class CommonDataBox {
    public static final int KVS_DB_COLUMN_INDEX_MAP_APP_ID = 0;
    public static final int KVS_DB_COLUMN_INDEX_MAP_KEY = 1;
    public static final int KVS_DB_COLUMN_INDEX_MAP_VALUE = 2;
    public static final String KVS_DB_COLUMN_NAME_APP_ID = "appid";
    public static final String KVS_DB_COLUMN_NAME_KEY = "key";
    public static final String KVS_DB_COLUMN_NAME_VALUE = "value";
    public static final String KVS_DB_KEY_CACHE_CLEAR_FLG = "cacheClearFlg";
    public static final String KVS_DB_KEY_NAME_CDN_ROOT = "cdn_root_url";
    public static final String KVS_DB_KEY_NAME_LINE = "line";
    public static final String KVS_DB_KEY_NAME_UPDATE_FINISH_DATE = "updateFinishDate";
    public static final String KVS_DB_KEY_NAME_ZEMI_KBN = "zemiKbn";
    public static final String KVS_DB_KEY_TIME_SYNC_DATE = "timeSyncDate";
    public static final String PATH_FILE_PROVEIDER_APPID = "/jp.co.benesse.touch.home/";
    public static final String PATH_FILE_PROVIDER_URI = "content://jp.co.benesse.dcha.databox.file.FileProvider/top_dir/";
    public static final String SBOX_KEY_NAME_CDN_ROOT = "cdn_root_url";
    public static final String SBOX_KEY_NAME_LINE = "line";
    public static final Uri PATH_KVS_CONTENT_URI = Uri.parse("content://jp.co.benesse.dcha.databox.db.KvsProvider/kvs/jp.co.benesse.touch.home");
    public static final Uri PATH_KVS_CONTENT_URI_FOR_UPDATE = Uri.parse("content://jp.co.benesse.dcha.databox.db.KvsProvider/kvs/jp.co.benesse.touch.update");
    public static final Uri PATH_KVS_CONTENT_URI_FOR_AUTH = Uri.parse("content://jp.co.benesse.dcha.databox.db.KvsProvider/kvs/jp.co.benesse.touch.auth");

    private CommonDataBox() {
    }
}
