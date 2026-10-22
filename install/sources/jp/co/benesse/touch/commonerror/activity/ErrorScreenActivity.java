package jp.co.benesse.touch.commonerror.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import jp.co.benesse.touch.common.touchconstant.CommonErrorCode;
import jp.co.benesse.touch.commonerror.R;
import jp.co.benesse.touch.commonerror.util.CommonErrorConstants;

public class ErrorScreenActivity extends CommonErrorBaseActivity implements View.OnClickListener {
    public static final String DATE_FORMAT_HH = "HH";
    private static final int ERROR_CODE_LENGTH = 14;
    private static final String TAG = "ErrorScreenActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String errorCd = getIntent().getStringExtra(CommonErrorConstants.ERROR_CD);
        if (!inputCheckErrorCd(errorCd)) {
            errorCd = CommonErrorConstants.ERROR_CD_INPUT_PARAMETER_ERROR;
        }
        sendAnalytics(errorCd);
        List<String> maintenanceList = Arrays.asList(getResources().getStringArray(R.array.maintenance_error_list));
        if (maintenanceList.contains(errorCd)) {
            setContentView(R.layout.maintenance_layout);
        } else {
            setContentView(R.layout.errorscreen_layout);
        }
        TextView outputAreaErrorCd = (TextView) findViewById(R.id.errorscreen_error_cd);
        if (CommonErrorCode.BAE_MAINTENANCE.equals(errorCd)) {
            StringBuffer errorCdBuffer = new StringBuffer(CommonErrorConstants.STRING_BAEMAINTENANCE_ERROR_CD);
            errorCdBuffer.append(errorCd);
            outputAreaErrorCd.setText(errorCdBuffer.toString());
        } else {
            StringBuffer errorCdBuffer2 = new StringBuffer(CommonErrorConstants.STRING_ERROR_CD);
            errorCdBuffer2.append(errorCd);
            outputAreaErrorCd.setText(errorCdBuffer2.toString());
        }
        this.buttonOk = (ImageView) findViewById(R.id.errorscreen_ok_btn);
        this.buttonOk.setOnClickListener(this);
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
        this.buttonOk.setEnabled(false);
        decide();
    }

    public boolean inputCheckErrorCd(String errorCd) {
        return errorCd != null && errorCd.length() == ERROR_CODE_LENGTH;
    }

    protected void sendAnalytics(String errorCode) {
        Intent intent = new Intent();
        intent.setClassName("jp.co.benesse.touch.asynccomm", CommonErrorConstants.CLASS_NAME_GA);
        intent.putExtra(CommonErrorConstants.GA_EXTRA_TRACKING_TYPE, 1);
        intent.putExtra(CommonErrorConstants.GA_EXTRA_EVENT_CATEGORY, CommonErrorConstants.GA_EVENT_CATEGORY_ERROR);
        intent.putExtra(CommonErrorConstants.GA_EXTRA_EVENT_ACTION, CommonErrorConstants.GA_EVENT_ACTION_ERROR_MESSAGE);
        intent.putExtra(CommonErrorConstants.GA_EXTRA_EVENT_LABEL, errorCode + CommonErrorConstants.GA_EVENT_LABEL_REPLACE_STRING);
        int currentTime = Integer.parseInt(getNowTime(DATE_FORMAT_HH));
        intent.putExtra(CommonErrorConstants.GA_EXTRA_EVENT_VALUE, currentTime);
        startService(intent);
    }

    private String getNowTime(String dateFormat) {
        SimpleDateFormat smd = new SimpleDateFormat(dateFormat, Locale.JAPAN);
        String ret = smd.format(new Date());
        return ret;
    }
}
