package jp.co.benesse.touch.comm;

import android.os.RemoteException;
import java.util.Map;
import jp.co.benesse.touch.util.UrlUtil;

public class SettingServerTimeRequest extends IHttpRequest.Stub {
    String uriString = UrlUtil.getUrlTime();

    @Override
    public String getUri() throws RemoteException {
        return this.uriString;
    }

    @Override
    public String getHttpRequestBody() throws RemoteException {
        return null;
    }

    @Override
    public void setHttpRequestBody(String reqString) throws RemoteException {
    }

    @Override
    public void setPostParams(Map params) throws RemoteException {
    }

    @Override
    public Map getPostParams() throws RemoteException {
        return null;
    }

    @Override
    public void setRequestId(String id) throws RemoteException {
    }

    @Override
    public String getRequestId() throws RemoteException {
        return null;
    }
}
