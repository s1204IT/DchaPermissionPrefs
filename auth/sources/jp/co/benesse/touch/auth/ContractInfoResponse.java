package jp.co.benesse.touch.auth;

import android.os.RemoteException;
import android.util.Xml;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import jp.co.benesse.touch.comm.IHttpResponse;
import jp.co.benesse.touch.common.touchconstant.AuthRequest;
import jp.co.benesse.touch.common.touchconstant.AuthResponse;
import org.xmlpull.v1.XmlPullParser;

public class ContractInfoResponse extends IHttpResponse.Stub {
    public static final String TAG = "MemberSiteContractInfoResponse";
    public int errCode;
    public String line;
    Condition responseWaitCond;
    Lock responseWaitLock;
    public int statusCode;
    ContractInfoUpdateThread.ContractInfoWaitingCheckListener waitResponseFlagListener;
    protected String xmlResponse;
    public String fncDecNo = "";
    public String sttCd = "";
    public String errCd = "";
    public String errMsg = "";
    public String year = null;
    public String gradeCode = null;
    public String zemiKubun = null;

    public ContractInfoResponse(String line, Lock responseWaitLock, Condition responseWaitCond, ContractInfoUpdateThread.ContractInfoWaitingCheckListener waitResponseFlagListener) {
        this.line = null;
        this.responseWaitLock = new ReentrantLock();
        this.responseWaitCond = this.responseWaitLock.newCondition();
        this.line = line;
        this.responseWaitLock = responseWaitLock;
        this.responseWaitCond = responseWaitCond;
        this.waitResponseFlagListener = waitResponseFlagListener;
    }

    @Override
    public void setResponseString(String resString) throws RemoteException {
        this.xmlResponse = resString;
    }

    @Override
    public void doProcess() throws RemoteException {
        try {
            try {
                XmlPullParser xpp = Xml.newPullParser();
                xpp.setInput(new StringReader(this.xmlResponse));
                String tag = "";
                Map<String, Map<String, String>> responseMap = new HashMap<>();
                String execLine = null;
                String execGnnCd = null;
                String execZemiKubun = null;
                for (int eventType = xpp.getEventType(); eventType != 1; eventType = xpp.next()) {
                    switch (eventType) {
                        case 2:
                            tag = xpp.getName();
                            if (execLine != null) {
                                Map<String, String> registMap = new HashMap<>();
                                registMap.put(AuthResponse.RESPONSE_XML_KEY_GNN_CD, execGnnCd);
                                registMap.put("zemiKbn", execZemiKubun);
                                responseMap.put(execLine, registMap);
                            }
                            break;
                        case 4:
                            if (tag != null && 1 != 0) {
                                if (tag.equals(AuthResponse.RESPONSE_XML_KEY_FNC_DEC_NO)) {
                                    this.fncDecNo = xpp.getText();
                                } else if (tag.equals(AuthResponse.RESPONSE_XML_KEY_STATUS_CD)) {
                                    this.sttCd = xpp.getText();
                                } else if (tag.equals(AuthResponse.RESPONSE_XML_KEY_ERROR_CD)) {
                                    this.errCd = xpp.getText();
                                } else if (tag.equals(AuthResponse.RESPONSE_XML_KEY_ERROR_MSG)) {
                                    this.errMsg = xpp.getText();
                                } else if (!tag.equals(AuthResponse.RESPONSE_XML_KEY_PERIOD_PRD_CTT_BEAN) && !tag.equals(AuthResponse.RESPONSE_XML_KEY_ARRAY_INDEX)) {
                                    if (tag.equals(AuthResponse.RESPONSE_XML_KEY_LINE)) {
                                        execLine = xpp.getText();
                                    } else if (!tag.equals(AuthResponse.RESPONSE_XML_KEY_YAR)) {
                                        if (tag.equals(AuthResponse.RESPONSE_XML_KEY_GNN_CD)) {
                                            execGnnCd = xpp.getText();
                                        } else if (tag.equals("zemiKbn")) {
                                            execZemiKubun = xpp.getText();
                                        }
                                    }
                                }
                            }
                            break;
                    }
                }
                if (execLine != null) {
                    Map<String, String> registMap2 = new HashMap<>();
                    registMap2.put(AuthResponse.RESPONSE_XML_KEY_GNN_CD, execGnnCd);
                    registMap2.put("zemiKbn", execZemiKubun);
                    responseMap.put(execLine, registMap2);
                }
                Map<String, String> targetMap = responseMap.get(this.line);
                if (this.sttCd == null || !this.sttCd.equals(AuthService.COMPLETE_NORMAL) || targetMap == null) {
                    this.zemiKubun = null;
                } else {
                    this.gradeCode = targetMap.get(AuthResponse.RESPONSE_XML_KEY_GNN_CD);
                    SboxAdapter.getInstance().setValue(AuthRequest.SBOX_GRADE_CODE, this.gradeCode);
                    this.zemiKubun = targetMap.get("zemiKbn");
                }
                this.responseWaitLock.lock();
                try {
                    this.waitResponseFlagListener.setFlag(true);
                    this.responseWaitCond.signal();
                } catch (Exception e) {
                } finally {
                    this.responseWaitLock.unlock();
                }
            } catch (Exception e2) {
                this.sttCd = AuthService.XML_PARSE_ERROR;
                this.zemiKubun = null;
                this.responseWaitLock.lock();
                try {
                    this.waitResponseFlagListener.setFlag(true);
                    this.responseWaitCond.signal();
                } catch (Exception e3) {
                } finally {
                    this.responseWaitLock.unlock();
                }
            }
        } catch (Throwable th) {
            this.responseWaitLock.lock();
            try {
                this.waitResponseFlagListener.setFlag(true);
                this.responseWaitCond.signal();
            } catch (Exception e4) {
            } finally {
                this.responseWaitLock.unlock();
            }
            throw th;
        }
    }

    @Override
    public void onError(String reqID, String uri, int errCode, String errMsg, int statusCode) throws RemoteException {
        this.errCode = errCode;
        this.errMsg = errMsg;
        this.statusCode = statusCode;
        this.responseWaitLock.lock();
        try {
            this.waitResponseFlagListener.setFlag(true);
            this.responseWaitCond.signal();
        } catch (Exception e) {
        } finally {
            this.responseWaitLock.unlock();
        }
    }

    @Override
    public void onProgress(long currentSize, long fullSize) throws RemoteException {
    }
}
