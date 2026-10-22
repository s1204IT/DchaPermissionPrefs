package jp.co.benesse.touch.commonerror.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import jp.co.benesse.touch.common.touchconstant.IntentName;
import jp.co.benesse.touch.commonerror.R;
import jp.co.benesse.touch.commonerror.util.CommonErrorConstants;

public class CommonErrorManageActivity extends Activity {
    private static final String HOME_ERROR_TE51_D107_2005 = "TE51-D107-2005";
    private static final String KEY_IS_START_HEALTH_CHECK = "key_is_start_health_check";
    private static final String KEY_IS_START_HOME = "key_is_created";
    private static final String PACKAGE_PRECHU_BROWSER = "jp.co.benesse.dcha.prechubrowser";
    private static final String TAG = "CommonErrorManageActivity";
    private boolean isStartHealthCheck;
    private boolean isStartHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplication().getPackageName().equals(PACKAGE_PRECHU_BROWSER)) {
            setContentView(R.layout.manage_layout);
        }
        if (savedInstanceState == null) {
            startErrorActivity(getIntent());
        } else {
            this.isStartHome = savedInstanceState.getBoolean(KEY_IS_START_HOME);
            this.isStartHealthCheck = savedInstanceState.getBoolean(KEY_IS_START_HEALTH_CHECK);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_IS_START_HOME, this.isStartHome);
        outState.putBoolean(KEY_IS_START_HEALTH_CHECK, this.isStartHealthCheck);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        if (this.isStartHealthCheck) {
            this.isStartHome = true;
            this.isStartHealthCheck = false;
        } else if (this.isStartHome) {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            intent.addFlags(270565376);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
            overridePendingTransition(0, 0);
        }
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 0:
                finish();
                break;
            case CommonErrorConstants.REQUEST_CODE_CALL_TO_WIFI_ERROR:
                startWifiError(resultCode);
                break;
            case CommonErrorConstants.REQUEST_CODE_CALL_TO_INTER_ERROR:
                startInternetConnectionError(resultCode);
                break;
            case CommonErrorConstants.REQUEST_CODE_CALL_TO_SYS_ERROR:
                startErrorActivity(data);
                break;
            case CommonErrorConstants.RESULT_CODE_ACTION_TYPE_COMMON_BTN_WIFI_TEST:
                startHealthCheck();
                this.isStartHealthCheck = true;
                break;
            default:
                Intent intent = new Intent("android.intent.action.MAIN");
                intent.addCategory("android.intent.category.HOME");
                if (getApplication().getPackageName().equals(PACKAGE_PRECHU_BROWSER)) {
                    intent.setFlags(IntentName.FLG_RCVR_START_HOME);
                } else {
                    intent.addFlags(270565376);
                }
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    public void startErrorActivity(Intent intent) {
        int requestCode = intent.getIntExtra(CommonErrorConstants.EXTRA_REQUEST_CODE, -1);
        switch (requestCode) {
            case CommonErrorConstants.REQUEST_CODE_CALL_TO_WIFI_ERROR:
                startWifiError(requestCode);
                break;
            case CommonErrorConstants.REQUEST_CODE_CALL_TO_INTER_ERROR:
                startInternetConnectionError(requestCode);
                break;
            case CommonErrorConstants.REQUEST_CODE_CALL_TO_LOGIN_ERROR:
                startLoginWindow(requestCode);
                break;
            case CommonErrorConstants.REQUEST_CODE_CALL_TO_SYS_ERROR:
                String errorCode = intent.getStringExtra(CommonErrorConstants.ERROR_CD);
                startErrorScreen(requestCode, errorCode);
                break;
            default:
                startErrorScreen(CommonErrorConstants.REQUEST_CODE_CALL_TO_SYS_ERROR, HOME_ERROR_TE51_D107_2005);
                break;
        }
    }

    public void startErrorScreen(int requestCode, String errorCd) {
        Intent intent = new Intent(this, (Class<?>) ErrorScreenActivity.class);
        intent.putExtra(CommonErrorConstants.ERROR_CD, errorCd);
        startActivityForResult(intent, requestCode);
        overridePendingTransition(0, 0);
    }

    public void startInternetConnectionError(int requestCode) {
        startActivityForResult(new Intent(this, (Class<?>) InternetConnectionErrorActivity.class), requestCode);
        overridePendingTransition(0, 0);
    }

    public void startWifiError(int requestCode) {
        startActivityForResult(new Intent(this, (Class<?>) WifiErrorActivity.class), requestCode);
        overridePendingTransition(0, 0);
    }

    public void startLoginWindow(int requestCode) {
        startActivityForResult(new Intent(this, (Class<?>) LoginWindowActivity.class), requestCode);
        overridePendingTransition(0, 0);
    }

    public void startHealthCheck() {
        Intent intent = new Intent();
        intent.setClassName(IntentName.PKG_USER_SETTING, IntentName.CLASS_HEALTH_CHECK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }
}
