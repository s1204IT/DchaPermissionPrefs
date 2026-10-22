package jp.co.benesse.touch.comm;

import android.os.RemoteException;
import java.util.Map;

public class BaeServerRequest extends IHttpRequest.Stub {
    private Map<String, String> paramMap;
    String reqId;
    String uriString;

    @Override
    public void setPostParams(Map params) throws RemoteException {
        this.paramMap = params;
    }

    @Override
    public Map<String, String> getPostParams() throws RemoteException {
        return this.paramMap;
    }

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
    public void setRequestId(String id) throws RemoteException {
        this.reqId = id;
    }

    @Override
    public String getRequestId() throws RemoteException {
        return this.reqId;
    }
}
