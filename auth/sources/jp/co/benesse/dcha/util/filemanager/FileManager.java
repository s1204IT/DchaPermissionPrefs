package jp.co.benesse.dcha.util.filemanager;

import android.content.Context;
import android.net.Uri;
import java.io.File;

public class FileManager {
    private static final String COMMON_FOLDER = "/commoncontents";
    private static final String SHARE_FOLDER_PATH_URI = "content://jp.co.benesse.dcha.common.appbase.datastore.file.FileProvider/top_dir";

    public static ResponseString getAbsFilePathFromRelativePath(Context context, int typeflg, String relativePath) {
        File targetPath;
        ResponseString ret = new ResponseString();
        if (typeflg < 1 || typeflg > 2) {
            ret.setResultCode(-1);
            ret.setMessageCode("TE01-71vu-2001");
        } else {
            String msgCode = chkPathParam(relativePath, true, false, 0, false);
            if (!msgCode.equals("")) {
                ret.setResultCode(-1);
                ret.setMessageCode(msgCode);
            } else {
                if (typeflg == 1) {
                    targetPath = context.getFilesDir();
                } else {
                    String pathStr = context.getContentResolver().getType(Uri.parse(SHARE_FOLDER_PATH_URI));
                    if (pathStr == null) {
                        ret.setResultCode(-1);
                        ret.setMessageCode("TE01-71vu-2006");
                    } else {
                        targetPath = new File(pathStr);
                    }
                }
                File filedir = openLocalFile(targetPath, relativePath);
                if (filedir == null) {
                    ret.setResultCode(-1);
                    ret.setMessageCode("TE01-71vu-4001");
                } else {
                    ret.setResultCode(0);
                    ret.setResultString(filedir.getPath());
                }
            }
        }
        return ret;
    }

    public static ResponseString getAbsFilePathFromComponent(Context context, String fileName, String gradeCode, String monthCode, String planCode, String textbookCode) {
        ResponseString ret = combinePathParam(fileName, gradeCode, monthCode, planCode, textbookCode);
        if (ret.getResultCode() != -1) {
            String path = ret.getResultString();
            String pathStr = context.getContentResolver().getType(Uri.parse(SHARE_FOLDER_PATH_URI));
            if (pathStr == null) {
                ret.setResultCode(-1);
                ret.setMessageCode("TE01-71vu-2007");
            } else {
                File sharePath = new File(pathStr + COMMON_FOLDER);
                File filedir = openLocalFile(sharePath, path);
                if (filedir == null) {
                    ret.setResultCode(-1);
                    ret.setMessageCode("TE01-71vu-4002");
                } else {
                    ret.setResultCode(0);
                    ret.setResultString(filedir.getPath());
                }
            }
        }
        return ret;
    }

    private static File openLocalFile(File targetPath, String path) {
        int index = path.lastIndexOf("/");
        if (index > -1) {
            File subDir = new File(targetPath, path.substring(0, index));
            if (!subDir.exists() && !subDir.mkdirs()) {
                return null;
            }
        }
        return new File(targetPath, path);
    }

    private static ResponseString combinePathParam(String fileName, String gradeCode, String monthCode, String planCode, String textbookCode) {
        ResponseString ret = new ResponseString();
        String msgCode = chkPathParam(fileName, true, false, 0, true);
        if (!msgCode.equals("")) {
            ret.setMessageCode(msgCode);
        } else {
            String msgCode2 = chkPathParam(gradeCode, true, true, 2, true);
            if (!msgCode2.equals("")) {
                ret.setMessageCode(msgCode2);
            } else {
                String msgCode3 = chkPathParam(monthCode, true, true, 2, true);
                if (!msgCode3.equals("")) {
                    ret.setMessageCode(msgCode3);
                } else {
                    String msgCode4 = chkPathParam(planCode, true, true, 2, true);
                    if (!msgCode4.equals("")) {
                        ret.setMessageCode(msgCode4);
                    } else {
                        String msgCode5 = chkPathParam(textbookCode, true, true, 3, true);
                        if (!msgCode5.equals("")) {
                            ret.setMessageCode(msgCode5);
                        } else {
                            ret.setResultCode(0);
                            ret.setResultString(gradeCode + "/" + monthCode + "/" + planCode + "/" + textbookCode + "/" + fileName);
                        }
                    }
                }
            }
        }
        return ret;
    }

    private static String chkPathParam(String path, boolean chkNullFlg, boolean chkLenFlg, int chkLen, boolean chkSlashFlg) {
        if (chkNullFlg && path == null) {
            return "TE01-71vu-2002";
        }
        if (chkNullFlg && path.equals("")) {
            return "TE01-71vu-2003";
        }
        if (chkLenFlg && path.length() != chkLen) {
            return "TE01-71vu-2004";
        }
        if (!chkSlashFlg || path.indexOf("/") == -1) {
            return "";
        }
        return "TE01-71vu-2005";
    }
}
