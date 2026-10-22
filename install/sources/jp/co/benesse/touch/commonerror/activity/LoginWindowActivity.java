package jp.co.benesse.touch.commonerror.activity;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jp.co.benesse.touch.common.touchconstant.AuthRequest;
import jp.co.benesse.touch.common.touchconstant.AuthResponse;
import jp.co.benesse.touch.common.touchconstant.CommResponse;
import jp.co.benesse.touch.common.touchconstant.IntentName;
import jp.co.benesse.touch.commonerror.R;
import jp.co.benesse.touch.commonerror.logic.LoginWindowLogic;
import jp.co.benesse.touch.commonerror.util.CommonErrorConstants;

public class LoginWindowActivity extends CommonErrorBaseActivity implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {
    private static final int PASSWORD_MIN_LENGTH = 4;
    private static final String TAG = "LoginWindowActivity";
    private int authResultCd;
    private LoginWindowLogic logic;
    private ImageView mCheckIcon;
    private ImageView mCheckbox;
    private EditText mEditTextId;
    private EditText mEditTextPassword;
    private ImageView mImageAlert;
    private ImageView mImageLockAlert;
    private boolean mIsCheckbox1Checked;
    public boolean mIsWifi;
    private Button mLoginButton;
    private LoginTask mLoginTask;
    private Button mNonMemberButton;
    private Button mQuitButton;
    private Condition resultWaitCondition;
    private boolean resultWaitFlg;
    private Lock resultWaitLock;
    public boolean isLoginResultReceiver = false;
    private BroadcastReceiver mLoginResultReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(IntentName.ACTION_COMM_SERVICE_RESPONSE)) {
                String requestid = intent.getStringExtra(CommResponse.RES_COMM_KEY_REQUEST_ID);
                int resultCd = intent.getIntExtra("resultcode", -1);
                String serverURI = intent.getStringExtra(CommResponse.RES_COMM_KEY_REQUEST_URI);
                if (requestid == null && serverURI != null) {
                    String[] splitArray = serverURI.split("/");
                    if (splitArray.length > 0) {
                        String callApi = splitArray[splitArray.length - 1];
                        if (AuthRequest.API_AUTH_USER.equals(callApi) && !LoginWindowActivity.this.resultWaitFlg && resultCd != 0) {
                            LoginWindowActivity.this.authResultCd = resultCd;
                            LoginWindowActivity.this.resultWaitLock.lock();
                            LoginWindowActivity.this.resultWaitFlg = true;
                            LoginWindowActivity.this.resultWaitCondition.signal();
                            LoginWindowActivity.this.resultWaitLock.unlock();
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            }
            if (action.equals(IntentName.ACTION_AUTH_USER_RESULT_RESPONSE)) {
                LoginWindowActivity.this.authResultCd = intent.getIntExtra("resultcode", AuthResponse.RES_AUTH_USER_OTHER_ERROR);
                LoginWindowActivity.this.resultWaitLock.lock();
                LoginWindowActivity.this.resultWaitFlg = true;
                LoginWindowActivity.this.resultWaitCondition.signal();
                LoginWindowActivity.this.resultWaitLock.unlock();
            }
        }
    };
    private BroadcastReceiver mWifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
            NetworkInfo info = cm.getNetworkInfo(1);
            if (info != null && info.isConnected()) {
                LoginWindowActivity.this.mIsWifi = true;
            } else {
                LoginWindowActivity.this.mIsWifi = false;
            }
        }
    };
    private final IntentFilter mFilterAuth = new IntentFilter();
    private final IntentFilter mFilterWiFi = new IntentFilter();

    public LoginWindowActivity() {
        this.mFilterWiFi.addAction("android.net.wifi.STATE_CHANGE");
        this.mFilterAuth.addAction(IntentName.ACTION_COMM_SERVICE_RESPONSE);
        this.mFilterAuth.addAction(IntentName.ACTION_AUTH_USER_RESULT_RESPONSE);
        this.mFilterAuth.addCategory(IntentName.CATEGORY_COMM_SERVICE_RESULT_RESPONSE);
        this.mFilterAuth.addCategory(IntentName.CATEGORY_AUTH_RESULT_RESPONSE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(3);
        this.logic = new LoginWindowLogic(this);
        setContentView(R.layout.user_certification_layout);
        this.logic.authbindService();
        this.mLoginButton = (Button) findViewById(R.id.usercertification_login_btn);
        this.mQuitButton = (Button) findViewById(R.id.usercertification_quit_btn);
        this.mNonMemberButton = (Button) findViewById(R.id.usercertification_non_member_btn);
        this.mImageAlert = (ImageView) findViewById(R.id.usercertification_alart);
        this.mImageAlert.setVisibility(4);
        this.mImageLockAlert = (ImageView) findViewById(R.id.lock_alert);
        this.mImageLockAlert.setVisibility(4);
        this.mEditTextId = (EditText) findViewById(R.id.usercertification_edittext_id);
        this.mEditTextPassword = (EditText) findViewById(R.id.usercertification_edittext_password);
        this.mCheckbox = (ImageView) findViewById(R.id.usercertification_checkbox);
        this.mCheckIcon = (ImageView) findViewById(R.id.usercertification_check);
        this.mCheckIcon.setVisibility(4);
        this.mLoginButton.setOnClickListener(this);
        this.mQuitButton.setOnClickListener(this);
        this.mEditTextId.addTextChangedListener(this);
        this.mEditTextId.setOnFocusChangeListener(this);
        this.mEditTextPassword.addTextChangedListener(this);
        this.mEditTextPassword.setOnFocusChangeListener(this);
        this.mCheckbox.setOnClickListener(this);
        if (IntentName.PKG_HOME.equals(getApplication().getPackageName())) {
            this.mNonMemberButton.setOnClickListener(this);
        } else {
            this.mNonMemberButton.setVisibility(8);
        }
        this.resultWaitLock = new ReentrantLock();
        this.resultWaitCondition = this.resultWaitLock.newCondition();
        changePasswordVisibility();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(this.mWifiReceiver, this.mFilterWiFi);
        changeEnable(true);
        this.mLoginButton.setEnabled(false);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (this.isLoginResultReceiver) {
            unregisterReceiver(this.mLoginResultReceiver);
        }
        if (this.mWifiReceiver != null) {
            unregisterReceiver(this.mWifiReceiver);
        }
        changeEnable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.logic.onDestroy();
        this.mLoginButton.setOnClickListener(null);
        this.mQuitButton.setOnClickListener(null);
        this.mNonMemberButton.setOnClickListener(null);
        this.mEditTextId.addTextChangedListener(null);
        this.mEditTextId.setOnFocusChangeListener(null);
        this.mEditTextPassword.addTextChangedListener(null);
        this.mEditTextPassword.setOnFocusChangeListener(null);
        this.mCheckbox.setOnClickListener(null);
        this.mLoginButton = null;
        this.mQuitButton = null;
        this.mNonMemberButton = null;
        this.mImageAlert = null;
        this.mImageLockAlert = null;
        this.mEditTextId = null;
        this.mEditTextPassword = null;
        this.mCheckbox = null;
        this.mCheckIcon = null;
        this.logic = null;
        this.mLoginTask = null;
        this.resultWaitLock = null;
        this.resultWaitCondition = null;
        this.mLoginResultReceiver = null;
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
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == this.mCheckbox.getId()) {
            changePasswordVisibility();
            return;
        }
        if (v.getId() == this.mLoginButton.getId()) {
            changeEnable(false);
            if (!this.mIsWifi) {
                callWifiError();
                return;
            }
            registerReceiver(this.mLoginResultReceiver, this.mFilterAuth);
            this.isLoginResultReceiver = true;
            this.mLoginTask = new LoginTask();
            this.mLoginTask.execute(new String[0]);
            return;
        }
        if (v.getId() == this.mQuitButton.getId()) {
            changeEnable(false);
            callBackActivity(CommonErrorConstants.RESULT_CODE_ACTION_TYPE_LOGIN_AUTH_CANCEL, null);
            return;
        }
        if (v.getId() == this.mNonMemberButton.getId()) {
            changeEnable(false);
            try {
                Intent intent = new Intent();
                if (this.logic.hasChuBrowser()) {
                    intent.setClassName(IntentName.PKG_CHU_BROWSER, IntentName.CHU_BROWSER_CLASS_BASE);
                } else {
                    intent.setClassName(IntentName.PKG_BROWSER, IntentName.CLASS_BASE);
                }
                intent.addFlags(IntentName.FLG_START_CHUZEMI_BROWSER);
                intent.putExtra(CommonErrorConstants.NON_MEMBER_FLG, true);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                overridePendingTransition(0, 0);
            } catch (ActivityNotFoundException e) {
                callErrorScreen(CommonErrorConstants.ERROR_CD_BROWSER_OPEN_FAILED);
            }
        }
    }

    class LoginTask extends AsyncTask<String, Integer, Integer> {
        LoginTask() {
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result.intValue() == 0) {
                LoginWindowActivity.this.callBackActivity(CommonErrorConstants.RESULT_CODE_ACTION_TYPE_LOGIN_AUTH_OK, null);
                return;
            }
            if (result.intValue() == 1 || result.intValue() == 1011) {
                LoginWindowActivity.this.resultWaitFlg = false;
                LoginWindowActivity.this.mImageAlert.setVisibility(0);
                LoginWindowActivity.this.mEditTextPassword.setText("");
                LoginWindowActivity.this.changeEnable(true);
                LoginWindowActivity.this.mLoginButton.setEnabled(false);
                return;
            }
            if (result.intValue() == 2) {
                LoginWindowActivity.this.resultWaitFlg = false;
                LoginWindowActivity.this.mImageAlert.setVisibility(4);
                LoginWindowActivity.this.mEditTextPassword.setText("");
                LoginWindowActivity.this.changeEnable(false);
                LoginWindowActivity.this.mQuitButton.setEnabled(true);
                LoginWindowActivity.this.mImageLockAlert.setVisibility(0);
                return;
            }
            if (result.intValue() == -2 || result.intValue() == -3 || result.intValue() == -4) {
                LoginWindowActivity.this.callInternetConnectionError();
            } else if (result.intValue() == -7) {
                LoginWindowActivity.this.callErrorScreen(CommonErrorConstants.ERROR_CD_LOGIN_FAILED_PAD);
            } else {
                LoginWindowActivity.this.callErrorScreen(CommonErrorConstants.ERROR_CD_LOGIN_FAILED_SERVER);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            LoginWindowActivity.this.changeEnable(false);
        }

        @Override
        protected Integer doInBackground(String... arg0) {
            LoginWindowActivity.this.resultWaitLock.lock();
            try {
                LoginWindowActivity.this.logic.loginCertification(LoginWindowActivity.this.mEditTextId.getText().toString(), LoginWindowActivity.this.mEditTextPassword.getText().toString());
                while (!LoginWindowActivity.this.resultWaitFlg) {
                    LoginWindowActivity.this.resultWaitCondition.await();
                }
                LoginWindowActivity.this.resultWaitFlg = false;
            } catch (Exception e) {
                LoginWindowActivity.this.authResultCd = -7;
            } finally {
                LoginWindowActivity.this.resultWaitLock.unlock();
            }
            return Integer.valueOf(LoginWindowActivity.this.authResultCd);
        }
    }

    @Override
    public void onFocusChange(View v, boolean flg) {
        if (v.getId() == this.mEditTextId.getId() && flg) {
            this.mEditTextId.setBackgroundResource(R.drawable.js002_04010_flame_current);
            return;
        }
        if (v.getId() == this.mEditTextId.getId() && !flg) {
            this.mEditTextId.setBackgroundResource(R.drawable.common_flame);
            return;
        }
        if (v.getId() == this.mEditTextPassword.getId() && flg) {
            this.mEditTextPassword.setBackgroundResource(R.drawable.js002_04010_flame_current);
        } else if (v.getId() == this.mEditTextPassword.getId() && !flg) {
            this.mEditTextPassword.setBackgroundResource(R.drawable.common_flame);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (!"".equals(this.mEditTextId.getText().toString()) && this.mEditTextPassword.getText().toString() != null && this.mEditTextPassword.getText().toString().length() >= 4) {
            this.mLoginButton.setEnabled(true);
        } else {
            this.mLoginButton.setEnabled(false);
        }
    }

    private void changePasswordVisibility() {
        if (this.mIsCheckbox1Checked) {
            this.mCheckIcon.setVisibility(0);
            this.mEditTextPassword.setInputType(145);
            this.mEditTextPassword.setTextColor(getResources().getColor(R.color.text_black));
            this.mEditTextPassword.setPadding(22, 0, 0, 0);
            this.mIsCheckbox1Checked = false;
        } else {
            this.mCheckIcon.setVisibility(4);
            this.mEditTextPassword.setInputType(129);
            this.mEditTextPassword.setTextColor(getResources().getColor(R.color.text_gray_hc));
            this.mEditTextPassword.setPadding(27, 0, 0, 0);
            this.mIsCheckbox1Checked = true;
        }
        Typeface tf = Typeface.create(CommonErrorConstants.FONT_FAMILY_NAME, 0);
        this.mEditTextPassword.setTypeface(tf);
    }

    public void callErrorScreen(String errorCd) {
        Intent intent = new Intent(this, (Class<?>) ErrorScreenActivity.class);
        intent.putExtra(CommonErrorConstants.ERROR_CD, errorCd);
        callBackActivity(CommonErrorConstants.REQUEST_CODE_CALL_TO_SYS_ERROR, intent);
    }

    public void callInternetConnectionError() {
        callBackActivity(CommonErrorConstants.REQUEST_CODE_CALL_TO_INTER_ERROR, null);
    }

    public void callWifiError() {
        callBackActivity(CommonErrorConstants.REQUEST_CODE_CALL_TO_WIFI_ERROR, null);
    }

    public TextView getmEditTextId() {
        return this.mEditTextId;
    }

    public TextView getmEditTextPassword() {
        return this.mEditTextPassword;
    }

    public void changeEnable(boolean flgEnable) {
        this.mLoginButton.setEnabled(flgEnable);
        this.mQuitButton.setEnabled(flgEnable);
        this.mNonMemberButton.setEnabled(flgEnable);
        this.mEditTextId.setEnabled(flgEnable);
        this.mEditTextPassword.setEnabled(flgEnable);
        this.mCheckbox.setEnabled(flgEnable);
    }
}
