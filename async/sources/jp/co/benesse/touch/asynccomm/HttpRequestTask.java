package jp.co.benesse.touch.asynccomm;

import android.os.AsyncTask;
import android.os.RemoteException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import jp.co.benesse.touch.auth.IAuthService;
import org.json.JSONException;
import org.json.JSONObject;

public class HttpRequestTask extends AsyncTask<SendRequestDto, Object, Integer> {
    public static final int HTTP_CONNECTION_TIMEOUT_MS = 30000;
    private static final int HTTP_STATUS_CLIENT_ERROR = 400;
    private static final int HTTP_STATUS_EXPIRED_SESSION = 470;
    private static final int HTTP_STATUS_OK = 200;
    private static final int HTTP_STATUS_SERVER_ERROR = 500;
    private static final int HTTP_STATUS_SESSION_NOT_FOUND = 460;
    private static final String JSON_KEY_CODE = "code";
    private static final String JSON_KEY_ERROR = "error";
    private static final String JSON_KEY_RESULT = "result";
    private static final int LOG_API_ERROR_CODE_USER_KEY = 2;
    private static final int LOG_API_ERROR_CODE_VALIDATE = 1;
    private static final String TAG = "HttpRequestTask";
    private IHttpResponse response;
    private AsyncCommService service;

    @Override
    protected void onPreExecute() {
    }

    public HttpRequestTask(AsyncCommService asyncCommService, IHttpResponse iHttpResponse) {
        this.service = asyncCommService;
        this.response = iHttpResponse;
    }

    @Override
    protected Integer doInBackground(SendRequestDto... sendRequestDtoArr) throws Throwable {
        HttpURLConnection httpURLConnectionCreateConnection;
        int iExecuteHttpConnection = 1002;
        String request = null;
        try {
            httpURLConnectionCreateConnection = createConnection(sendRequestDtoArr[0]);
            try {
                request = sendRequestDtoArr[0].getRequest();
                iExecuteHttpConnection = 0;
            } catch (RemoteException | IOException unused) {
            } catch (MalformedURLException unused2) {
                iExecuteHttpConnection = 1003;
            }
        } catch (RemoteException | IOException unused3) {
            httpURLConnectionCreateConnection = null;
        } catch (MalformedURLException unused4) {
            httpURLConnectionCreateConnection = null;
        }
        if (iExecuteHttpConnection == 0) {
            iExecuteHttpConnection = executeHttpConnection(httpURLConnectionCreateConnection, request);
        }
        return Integer.valueOf(iExecuteHttpConnection);
    }

    @Override
    protected void onPostExecute(Integer num) {
        if (isCancelled()) {
            return;
        }
        this.response.onRespose(num.intValue());
    }

    private HttpURLConnection createConnection(SendRequestDto sendRequestDto) throws RemoteException, IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(sendRequestDto.getUrl()).openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setConnectTimeout(HTTP_CONNECTION_TIMEOUT_MS);
        httpURLConnection.setRequestProperty("X-API-Version", "1.0");
        httpURLConnection.setRequestProperty("X-Caller-Id", "TouchAsyncCommService");
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        IAuthService authService = this.service.getAuthService();
        httpURLConnection.setRequestProperty(authService.getSessionHeaderName(true), authService.getHttpsSessionKey());
        return httpURLConnection;
    }

    private int executeHttpConnection(HttpURLConnection httpURLConnection, String str) throws Throwable {
        OutputStream outputStream;
        int i = 0;
        if (isCancelled()) {
            return 0;
        }
        PrintStream printStream = null;
        int i2 = IHttpResponse.RESULT_ERROR_CONNECT_SYSTEM;
        try {
            httpURLConnection.connect();
            outputStream = httpURLConnection.getOutputStream();
            try {
                PrintStream printStream2 = new PrintStream(outputStream);
                try {
                    if (isCancelled()) {
                        printStream2.close();
                        if (outputStream != null) {
                            try {
                                outputStream.close();
                            } catch (IOException unused) {
                            }
                        }
                        if (httpURLConnection != null) {
                            httpURLConnection.disconnect();
                        }
                        return 0;
                    }
                    printStream2.print(str);
                    int responseCode = httpURLConnection.getResponseCode();
                    this.response.setStatusCode(responseCode);
                    if (responseCode == HTTP_STATUS_OK) {
                        if (isCancelled()) {
                            printStream2.close();
                            if (outputStream != null) {
                                try {
                                    outputStream.close();
                                } catch (IOException unused2) {
                                }
                            }
                            if (httpURLConnection != null) {
                                httpURLConnection.disconnect();
                            }
                            return 0;
                        }
                        JSONObject jSONObject = new JSONObject(getResponseString(httpURLConnection));
                        this.response.setResponse(jSONObject.toString());
                        if (!jSONObject.has("result")) {
                            if (jSONObject.has("error")) {
                                int i3 = jSONObject.getJSONObject("error").getInt("code");
                                if (i3 == 1) {
                                    i = IHttpResponse.RESULT_ERROR_RESPONSE_VALIDATE;
                                } else if (i3 == 2) {
                                    i = IHttpResponse.RESULT_ERROR_RESPONSE_USER_KEY;
                                } else {
                                    i = IHttpResponse.RESULT_ERROR_RESPONSE_UNEXPECTED;
                                }
                            } else {
                                i = IHttpResponse.RESULT_ERROR_RESPONSE_UNEXPECTED;
                            }
                        }
                        i2 = i;
                    } else if (responseCode == HTTP_STATUS_SESSION_NOT_FOUND || responseCode == HTTP_STATUS_EXPIRED_SESSION) {
                        i2 = IHttpResponse.RESULT_ERROR_CONNECT_SESSION;
                    } else if (responseCode >= HTTP_STATUS_CLIENT_ERROR && responseCode < HTTP_STATUS_SERVER_ERROR) {
                        i2 = IHttpResponse.RESULT_ERROR_CONNECT_CLIENT;
                    } else if (responseCode >= HTTP_STATUS_SERVER_ERROR) {
                        i2 = IHttpResponse.RESULT_ERROR_CONNECT_SERVER;
                    }
                    printStream2.close();
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException unused3) {
                        }
                    }
                    if (httpURLConnection != null) {
                    }
                    return i2;
                } catch (IOException unused4) {
                    printStream = printStream2;
                    if (printStream != null) {
                        printStream.close();
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException unused5) {
                        }
                    }
                    if (httpURLConnection != null) {
                    }
                    return i2;
                } catch (JSONException unused6) {
                    printStream = printStream2;
                    if (printStream != null) {
                        printStream.close();
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException unused7) {
                        }
                    }
                    if (httpURLConnection != null) {
                    }
                    return i2;
                } catch (Throwable th) {
                    th = th;
                    printStream = printStream2;
                    if (printStream != null) {
                        printStream.close();
                    }
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException unused8) {
                        }
                    }
                    if (httpURLConnection == null) {
                        throw th;
                    }
                    httpURLConnection.disconnect();
                    throw th;
                }
            } catch (IOException unused9) {
            } catch (JSONException unused10) {
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException unused11) {
            outputStream = null;
        } catch (JSONException unused12) {
            outputStream = null;
        } catch (Throwable th3) {
            th = th3;
            outputStream = null;
        }
        httpURLConnection.disconnect();
        return i2;
    }

    private String getResponseString(HttpURLConnection httpURLConnection) throws Throwable {
        InputStream inputStream;
        StringBuffer stringBuffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            inputStream = httpURLConnection.getInputStream();
            try {
                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                while (true) {
                    try {
                        String line = bufferedReader2.readLine();
                        if (line != null) {
                            stringBuffer.append(line);
                        } else {
                            try {
                                break;
                            } catch (IOException unused) {
                            }
                        }
                    } catch (IOException unused2) {
                        bufferedReader = bufferedReader2;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException unused3) {
                            }
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException unused4) {
                            }
                        }
                    } catch (Throwable th) {
                        th = th;
                        bufferedReader = bufferedReader2;
                        if (bufferedReader != null) {
                            try {
                                bufferedReader.close();
                            } catch (IOException unused5) {
                            }
                        }
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                                throw th;
                            } catch (IOException unused6) {
                                throw th;
                            }
                        }
                        throw th;
                    }
                }
                bufferedReader2.close();
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException unused7) {
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (IOException unused8) {
            inputStream = null;
        } catch (Throwable th3) {
            th = th3;
            inputStream = null;
        }
        return stringBuffer.toString();
    }
}
