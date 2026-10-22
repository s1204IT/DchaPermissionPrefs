package jp.co.benesse.touch.auth;

import android.os.RemoteException;
import jp.co.benesse.touch.comm.IHttpResponse;

public abstract class AuthServerResponse extends IHttpResponse.Stub {
    protected String jsonResponse;

    public abstract void doProcess() throws RemoteException;

    @Override
    public void setResponseString(String jsonString) throws RemoteException {
        this.jsonResponse = jsonString;
    }

    int toIntCode(String resultCode) {
        if (resultCode == null) {
            return -1;
        }
        int ret = Integer.parseInt(resultCode.replaceAll("[^0-9]", ""));
        return ret;
    }
}
