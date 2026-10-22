package jp.co.benesse.touch.auth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sbox {
    public static final String TAG = "Sbox";
    private static Sbox instance;

    private static native String getJSONString();

    private static native int setJSONString(String str);

    static {
        System.loadLibrary("sbox2");
    }

    private Sbox() {
    }

    public static synchronized Sbox getInstance() {
        if (instance == null) {
            instance = new Sbox();
        }
        return instance;
    }

    public synchronized String getValue(String key) {
        String ret;
        ret = null;
        try {
            String jsonString = getJSONString();
            if (jsonString != null) {
                JSONObject rootJson = new JSONObject(jsonString);
                String value = rootJson.getString(key);
                ret = value != null ? value : null;
            }
        } catch (JSONException e) {
        } catch (Exception e2) {
        }
        return ret;
    }

    public synchronized void setValue(String key, String value) {
        JSONObject rootJson;
        try {
            try {
                String jsonString = getJSONString();
                if (jsonString != null) {
                    try {
                        rootJson = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        rootJson = new JSONObject();
                    }
                } else {
                    rootJson = new JSONObject();
                }
                rootJson.putOpt(key, value);
                setJSONString(rootJson.toString());
            } catch (JSONException e2) {
            }
        } catch (Exception e3) {
        }
    }

    public synchronized JSONArray getArrayValues(String key) {
        JSONArray ret;
        ret = null;
        try {
            String jsonString = getJSONString();
            if (jsonString != null) {
                JSONObject rootJson = new JSONObject(jsonString);
                ret = rootJson.getJSONArray(key);
            }
        } catch (JSONException e) {
        } catch (Exception e2) {
        }
        return ret;
    }

    public synchronized void setArrayValues(String key, JSONArray values) {
        JSONObject rootJson;
        try {
            try {
                String jsonString = getJSONString();
                if (jsonString != null) {
                    try {
                        rootJson = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        rootJson = new JSONObject();
                    }
                } else {
                    rootJson = new JSONObject();
                }
                rootJson.putOpt(key, values);
                setJSONString(rootJson.toString());
            } catch (JSONException e2) {
            }
        } catch (Exception e3) {
        }
    }
}
