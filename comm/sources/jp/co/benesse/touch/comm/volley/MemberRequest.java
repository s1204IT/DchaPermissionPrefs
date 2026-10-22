package jp.co.benesse.touch.comm.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import java.util.HashMap;
import java.util.Map;

public abstract class MemberRequest<T> extends Request<T> {
    private byte[] bodyArray;
    private Map<String, String> headers;

    public MemberRequest(int method, String url, Response.ErrorListener listener) {
        super(method, url, listener);
        this.headers = new HashMap();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return this.headers;
    }

    public void setHeader(String key, String value) {
        this.headers.put(key, value);
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        return this.bodyArray;
    }

    public void setBody(byte[] array) {
        this.bodyArray = array;
    }
}
