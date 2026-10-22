package jp.co.benesse.touch.commonerror.activity;

import android.app.Activity;
import android.content.Intent;
import android.widget.ImageButton;
import android.widget.ImageView;
import jp.co.benesse.touch.commonerror.util.CommonErrorConstants;

public abstract class CommonErrorBaseActivity extends Activity {
    protected ImageView buttonOk;
    protected ImageButton healthCheckBtn;
    private final String TAG = "CommonErrorBaseActivity";
    protected boolean isFinActivity = false;

    protected void callBackActivity(int resultCode, Intent intent) {
        setResult(resultCode, intent);
        this.isFinActivity = true;
        finish();
        overridePendingTransition(0, 0);
    }

    protected void decide() {
        this.buttonOk.setEnabled(false);
        callBackActivity(CommonErrorConstants.RESULT_CODE_ACTION_TYPE_COMMON_BTN_OK, null);
    }

    protected void healthCheck() {
        callBackActivity(CommonErrorConstants.RESULT_CODE_ACTION_TYPE_COMMON_BTN_WIFI_TEST, null);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!this.isFinActivity) {
            setResult(0);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.buttonOk != null) {
            this.buttonOk.setOnClickListener(null);
        }
        this.buttonOk = null;
        if (this.healthCheckBtn != null) {
            this.healthCheckBtn.setOnClickListener(null);
        }
        this.healthCheckBtn = null;
    }
}
