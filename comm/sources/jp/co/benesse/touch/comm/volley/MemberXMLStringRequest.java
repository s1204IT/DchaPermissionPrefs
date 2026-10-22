package jp.co.benesse.touch.comm.volley;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemberXMLStringRequest extends MemberRequest<String> {
    private static final String PROTOCOL_CHARSET = "utf-8";
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/xml; charset=%s", PROTOCOL_CHARSET);
    private static final String TAG = "DchaXMLStringRequest";
    private Response.Listener<String> listener;

    public MemberXMLStringRequest(int method, String url, Response.Listener<String> listener, Response.ErrorListener errListener) {
        super(method, url, errListener);
        this.listener = listener;
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String resBodyString = new String(response.data, "UTF-8");
            Pattern pattern = Pattern.compile(">[ \t\n\f\r]*<");
            Matcher matcher = pattern.matcher(resBodyString);
            String resBodyString2 = matcher.replaceAll("><");
            return Response.success(resBodyString2, getCacheEntry());
        } catch (UnsupportedEncodingException e) {
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
    protected void deliverResponse(String response) {
        this.listener.onResponse(response);
    }
}
