package jp.co.benesse.touch.comm.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class AuthJsonRequest extends Request<JSONObject> {
    public static final String TAG = "AuthJsonRequest";
    private final Response.Listener<JSONObject> listener;
    private Map<String, String> paramMap;
    private Request.Priority priority;

    public AuthJsonRequest(int method, String url, Response.Listener<JSONObject> jsonListener, Response.ErrorListener errListener) {
        super(method, url, errListener);
        this.priority = Request.Priority.NORMAL;
        this.listener = jsonListener;
    }

    public void setParams(Map<String, String> map) {
        this.paramMap = map;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.paramMap;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String resBodyString = new String(response.data, "UTF-8");
            try {
                JSONObject result = new JSONObject(resBodyString);
                return Response.success(result, getCacheEntry());
            } catch (JSONException e) {
                return null;
            }
        } catch (UnsupportedEncodingException e2) {
            return null;
        }
    }

    private String parseResponseData(byte[] data, String encode) {
        int offset = 4;
        try {
            String res = new String(data, encode);
            int pos = res.indexOf("\r\n\r\n");
            if (pos == -1) {
                pos = res.indexOf("\n\n");
                offset = 2;
            }
            if (pos != -1) {
                String ret = res.substring(pos + offset);
                return ret;
            }
            return res;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        this.listener.onResponse(response);
    }

    @Override
    public Request.Priority getPriority() {
        return this.priority;
    }

    public void setPriority(Request.Priority priority) {
        this.priority = priority;
    }
}
