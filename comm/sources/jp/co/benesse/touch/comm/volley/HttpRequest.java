package jp.co.benesse.touch.comm.volley;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.Map;

public class HttpRequest extends StringRequest {
    private Map<String, String> paramMap;

    public HttpRequest(int method, String url, Response.Listener<String> strListener, Response.ErrorListener errListener) {
        super(method, url, strListener, errListener);
    }

    public void setParams(Map<String, String> map) {
        this.paramMap = map;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return this.paramMap;
    }
}
