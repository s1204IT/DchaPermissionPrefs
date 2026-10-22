package jp.co.benesse.touch.auth;

import android.os.RemoteException;
import java.util.Map;
import java.util.UUID;
import jp.co.benesse.touch.comm.IHttpRequest;
import jp.co.benesse.touch.util.UrlUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class BaeServerRequest extends IHttpRequest.Stub {
    public static final String ID = "id";
    public static final String JSON_RPC = "jsonrpc";
    public static final String METHOD = "method";
    public static final String PARAMS = "params";
    String reqId;
    protected JSONObject requestJson = new JSONObject();
    String uriString = UrlUtil.getUrlBae();

    @Override
    public void setPostParams(Map params) throws RemoteException {
    }

    @Override
    public Map<String, String> getPostParams() throws RemoteException {
        return null;
    }

    @Override
    public String getUri() throws RemoteException {
        return this.uriString;
    }

    @Override
    public String getHttpRequestBody() throws RemoteException {
        return this.requestJson.toString();
    }

    @Override
    public void setHttpRequestBody(String reqString) throws RemoteException {
        try {
            this.requestJson = new JSONObject(reqString);
        } catch (Exception e) {
            this.requestJson = null;
        }
    }

    @Override
    public void setRequestId(String id) throws RemoteException {
        this.reqId = id;
    }

    @Override
    public String getRequestId() throws RemoteException {
        return this.reqId;
    }

    protected JSONObject getBaseJSON() throws RemoteException {
        JSONObject base = new JSONObject();
        try {
            base.put(JSON_RPC, "2.0");
            base.put(METHOD, "common.akamaiToken.get");
            base.put("id", UUID.randomUUID().toString());
            return base;
        } catch (JSONException e) {
            throw new RemoteException();
        }
    }
}
