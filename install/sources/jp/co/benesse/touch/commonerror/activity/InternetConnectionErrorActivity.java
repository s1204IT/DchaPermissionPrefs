package jp.co.benesse.touch.commonerror.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import jp.co.benesse.touch.common.touchconstant.BrowserRequest;
import jp.co.benesse.touch.commonerror.R;
import jp.co.benesse.touch.commonerror.util.CommonErrorConstants;
import jp.co.benesse.touch.commonerror.util.TouchNetworkUtil;

public class InternetConnectionErrorActivity extends CommonErrorBaseActivity implements View.OnClickListener {
    private final String TAG = "InternetConnectionErrorActivity";
    private int btnCheckId;
    private int btnOkId;
    private int layoutResID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateComponentId();
        setContentView(this.layoutResID);
        this.buttonOk = (ImageView) findViewById(this.btnOkId);
        this.buttonOk.setOnClickListener(this);
        this.healthCheckBtn = (ImageButton) findViewById(this.btnCheckId);
        this.healthCheckBtn.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected void onRestart() {
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        Intent intent = getIntent();
        if (intent != null && BrowserRequest.SCREEN_ID.equals(intent.getStringExtra(CommonErrorConstants.SCREEN_ID))) {
            finish();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.internetconnectionerror_ok_btn || id == R.id.serverbusyerror_ok_btn) {
            decide();
        } else if (id == R.id.internetconnectionerror_hCheck_btn || id == R.id.serverbusyerror_hCheck_btn) {
            healthCheck();
        }
    }

    private void onCreateComponentId() {
        boolean connective = TouchNetworkUtil.isConnective(this);
        if (connective) {
            this.layoutResID = R.layout.server_busy_error_layout;
            this.btnOkId = R.id.serverbusyerror_ok_btn;
            this.btnCheckId = R.id.serverbusyerror_hCheck_btn;
        } else {
            this.layoutResID = R.layout.internetconnectionerror_layout;
            this.btnOkId = R.id.internetconnectionerror_ok_btn;
            this.btnCheckId = R.id.internetconnectionerror_hCheck_btn;
        }
    }
}
