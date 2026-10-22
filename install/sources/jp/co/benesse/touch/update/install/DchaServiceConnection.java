package jp.co.benesse.touch.update.install;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import java.util.ArrayList;
import java.util.List;
import jp.co.benesse.dcha.dchaservice.IDchaService;

public class DchaServiceConnection implements ServiceConnection {
    private List<DchaServiceConnectedCallback> callbacks;
    private IDchaService dchaService;

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        this.dchaService = IDchaService.Stub.asInterface(service);
        if (this.callbacks != null) {
            for (DchaServiceConnectedCallback callback : this.callbacks) {
                callback.onServiceConnected(this.dchaService);
            }
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        clearCallbacks();
        this.callbacks = null;
        this.dchaService = null;
    }

    public IDchaService getService() {
        return this.dchaService;
    }

    public boolean addCallback(DchaServiceConnectedCallback callback) {
        if (this.callbacks == null) {
            this.callbacks = new ArrayList();
        }
        return this.callbacks.add(callback);
    }

    private void clearCallbacks() {
        if (this.callbacks != null) {
            for (DchaServiceConnectedCallback callback : this.callbacks) {
                callback.onServiceDisconnected();
            }
            this.callbacks.clear();
        }
    }
}
