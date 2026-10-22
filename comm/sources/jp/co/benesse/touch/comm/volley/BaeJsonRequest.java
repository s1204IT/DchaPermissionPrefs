package jp.co.benesse.touch.comm.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class BaeJsonRequest extends Request<JSONObject> {
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);
    public static final String TAG = "BaeJsonRequest";
    private byte[] bodyArray;
    private Map<String, String> headers;
    private final Response.Listener<JSONObject> listener;
    private Request.Priority priority;

    public BaeJsonRequest(int method, String url, Response.Listener<JSONObject> jsonListener, Response.ErrorListener errListener) {
        super(method, url, errListener);
        this.headers = new HashMap();
        this.priority = Request.Priority.NORMAL;
        this.listener = jsonListener;
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return this.headers;
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
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

    @Override
    public byte[] getBody() throws AuthFailureError {
        return this.bodyArray;
    }

    public void setBody(byte[] array) {
        this.bodyArray = array;
    }
}
