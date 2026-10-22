package jp.co.benesse.touch.auth;

import android.os.RemoteException;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AkamaiTokenRequest extends BaeServerRequest {
    public static final String PARAM_ACCESS_LIST = "accessList";
    public static final String PARAM_URL = "url";
    private List<String> accessList;
    private String url;

    public AkamaiTokenRequest(String url, List<String> accessList) {
        this.url = url;
        this.accessList = accessList;
        createJsonRequestBody();
    }

    private void createJsonRequestBody() {
        try {
            this.requestJson = getBaseJSON();
            this.requestJson.put(BaeServerRequest.METHOD, "common.akamaiToken.get");
            JSONObject params = new JSONObject();
            if (this.url != null || this.accessList != null) {
                if (this.url != null) {
                    params.put("url", this.url);
                }
                if (this.accessList != null) {
                    JSONArray accessListJson = new JSONArray();
                    for (int i = 0; i < this.accessList.size(); i++) {
                        accessListJson.put(this.accessList.get(i));
                    }
                    params.put(PARAM_ACCESS_LIST, accessListJson);
                }
                this.requestJson.put(BaeServerRequest.PARAMS, params);
            }
        } catch (RemoteException e) {
        } catch (JSONException e2) {
        }
    }
}
