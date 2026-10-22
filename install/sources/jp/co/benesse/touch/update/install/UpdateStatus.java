package jp.co.benesse.touch.update.install;

import android.content.Context;
import android.content.SharedPreferences;
import jp.co.benesse.touch.common.touchconstant.PreferKey;

public class UpdateStatus {
    private static final String ACTION_CLOSE_SYSTEM_DIALOGS_RECEIVE_FLAG = "actionCloseSystemDialogsReceiveFlag";
    private final SharedPreferences pref;

    public UpdateStatus(Context context) {
        this.pref = context.getSharedPreferences(PreferKey.KEY_UPDATE_STATUS, 0);
    }

    public boolean isNowUpdate() {
        return this.pref.getBoolean(PreferKey.KEY_NOW_UPDATE, false);
    }

    public void setNowUpdate(Boolean nowUpdate) {
        SharedPreferences.Editor editor = this.pref.edit();
        editor.putBoolean(PreferKey.KEY_NOW_UPDATE, nowUpdate.booleanValue());
        editor.commit();
    }

    public int getUpdateType() {
        return this.pref.getInt(PreferKey.KEY_UPDATE_TYPE, 0);
    }

    public void setUpdateType(int updateType) {
        SharedPreferences.Editor editor = this.pref.edit();
        editor.putInt(PreferKey.KEY_UPDATE_TYPE, updateType);
        editor.commit();
    }

    public int getUpdateMode() {
        return this.pref.getInt(PreferKey.KEY_UPDATE_MODE, 0);
    }

    public void setUpdateMode(int updateMode) {
        SharedPreferences.Editor editor = this.pref.edit();
        editor.putInt(PreferKey.KEY_UPDATE_MODE, updateMode);
        editor.commit();
    }

    public boolean getActionCloseSystemDialogsReceiveFlg() {
        return this.pref.getBoolean(ACTION_CLOSE_SYSTEM_DIALOGS_RECEIVE_FLAG, false);
    }

    public void setActionCloseSystemDialogsReceiveFlg(boolean actionCloseSystemDialogsReceiveFlag) {
        SharedPreferences.Editor editor = this.pref.edit();
        editor.putBoolean(ACTION_CLOSE_SYSTEM_DIALOGS_RECEIVE_FLAG, actionCloseSystemDialogsReceiveFlag);
        editor.commit();
    }
}
