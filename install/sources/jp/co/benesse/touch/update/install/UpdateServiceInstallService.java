package jp.co.benesse.touch.update.install;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import java.util.ArrayList;
import jp.co.benesse.dcha.dchaservice.IDchaService;
import jp.co.benesse.touch.common.touchconstant.IntentName;
import jp.co.benesse.touch.commonerror.activity.CommonErrorManageActivity;
import jp.co.benesse.touch.commonerror.util.CommonErrorConstants;
import jp.co.benesse.touch.update.dto.UpdateInstallInfoDto;
import jp.co.benesse.touch.update.dto.UpdateInstallInfoDtoList;

public class UpdateServiceInstallService extends Service {
    private static final String EXTRA_PATH = "EXTRA_PATH";
    private static final String MESSAGE_UPDATE_INSTALL_SERVICE_2001 = "TE51-G101-2001";
    private static final String MESSAGE_UPDATE_INSTALL_SERVICE_2002 = "TE51-G101-2002";
    private static final String MESSAGE_UPDATE_INSTALL_SERVICE_2003 = "TE51-G101-2003";
    private static final String TAG = "UpdateServiceInstallService";
    private DchaServiceConnection dchaserviceConnection;
    private boolean forceReboot;
    private ArrayList<UpdateInstallInfoDto> installInfoList;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.dchaserviceConnection = new DchaServiceConnection();
        Intent intent = new Intent(IntentName.ACTION_DCHA_SERVICE);
        intent.setPackage(IntentName.PKG_DCHA_SERVICE);
        bindService(intent, this.dchaserviceConnection, 1);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        UpdateInstallInfoDtoList parcelable;
        if ((flags & 1) != 0 || (flags & 2) != 0) {
            this.forceReboot = true;
        }
        this.installInfoList = null;
        if (intent != null && (parcelable = (UpdateInstallInfoDtoList) intent.getParcelableExtra(EXTRA_PATH)) != null) {
            this.installInfoList = parcelable.getInstallinfolist();
        }
        if (this.dchaserviceConnection.getService() == null) {
            DchaServiceConnectedCallback callback = new DchaServiceConnectedCallback() {
                @Override
                public void onServiceConnected(IDchaService service) {
                    UpdateServiceInstallService.this.run(service);
                }
            };
            this.dchaserviceConnection.addCallback(callback);
        } else {
            run(this.dchaserviceConnection.getService());
        }
        return 1;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (this.dchaserviceConnection != null) {
            if (this.dchaserviceConnection.getService() != null) {
                unbindService(this.dchaserviceConnection);
            }
            this.dchaserviceConnection = null;
        }
    }

    public void run(IDchaService dchaService) {
        boolean rebootFlag = true;
        try {
            try {
                try {
                    setUpdateStatus();
                    if (this.installInfoList == null || this.installInfoList.size() == 0 || this.forceReboot) {
                        dchaService.rebootPad(0, null);
                    } else {
                        for (UpdateInstallInfoDto list : this.installInfoList) {
                            if (!list.getPath().isEmpty()) {
                                boolean installret = dchaService.installApp(list.getPath(), list.getType());
                                if (!installret) {
                                    rebootFlag = false;
                                    startSystemErrorActivity(MESSAGE_UPDATE_INSTALL_SERVICE_2001);
                                    break;
                                }
                            }
                        }
                        if (rebootFlag) {
                            dchaService.rebootPad(0, null);
                        }
                    }
                    stopSelf();
                } catch (RemoteException e) {
                    startSystemErrorActivity(MESSAGE_UPDATE_INSTALL_SERVICE_2002);
                    stopSelf();
                }
            } catch (PackageManager.NameNotFoundException e2) {
                startSystemErrorActivity(MESSAGE_UPDATE_INSTALL_SERVICE_2003);
                stopSelf();
            } catch (RuntimeException e3) {
                startSystemErrorActivity(MESSAGE_UPDATE_INSTALL_SERVICE_2003);
                stopSelf();
            }
        } catch (Throwable th) {
            stopSelf();
            throw th;
        }
    }

    private void startSystemErrorActivity(String errorCode) {
        startActivity(new Intent(this, (Class<?>) CommonErrorManageActivity.class).putExtra(CommonErrorConstants.EXTRA_REQUEST_CODE, CommonErrorConstants.REQUEST_CODE_CALL_TO_SYS_ERROR).putExtra(CommonErrorConstants.ERROR_CD, errorCode).setFlags(268468224));
    }

    private void setUpdateStatus() throws PackageManager.NameNotFoundException {
        Context mContext = createPackageContext(IntentName.PKG_UPDATE_SERVICE, 1);
        UpdateStatus us = new UpdateStatus(mContext);
        us.setNowUpdate(false);
        us.setUpdateType(4);
        us.setUpdateMode(0);
    }
}
