package jp.co.benesse.touch.auth;

import android.content.Context;
import android.os.Build;
import android.os.RemoteException;
import android.util.Xml;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import jp.co.benesse.dcha.util.TabletInfo;
import jp.co.benesse.touch.comm.IHttpRequest;
import jp.co.benesse.touch.common.touchconstant.AuthRequest;
import jp.co.benesse.touch.util.UrlUtil;
import org.xmlpull.v1.XmlSerializer;

public class ContractInfoRequest extends IHttpRequest.Stub {
    static final String DEC_TYPE_D02 = "D02";
    static final String DEC_TYPE_D03 = "D03";
    static final String DEC_TYPE_D12 = "D12";
    static final String DEC_TYPE_Q01 = "Q01";
    static final String DEC_TYPE_Q02 = "Q02";
    public static String FNC_DEC_NO_CT = "CT";
    static final String LINE_UP_CODE_ZM = "ZM";
    static final String PRODUCT_ASPECT_CLASS_PERIOD = "period";
    public static final String TAG = "DchaGetServerTimeRequest";
    static final String TEXTBOOK_INFO = "TextbookInformation";
    private Map<String, String> paramMap;
    String reqId;
    private final String serial;
    String uriString = UrlUtil.getUrlBaeMember();
    String xmlString;

    public ContractInfoRequest(Context context) throws Throwable {
        this.serial = TabletInfo.getSerial(context, Build.SERIAL);
        createXmlRequestBody();
    }

    protected void createXmlRequestBody() throws Throwable {
        this.xmlString = null;
        ByteArrayOutputStream os = null;
        XmlSerializer serializer = Xml.newSerializer();
        try {
            ByteArrayOutputStream os2 = new ByteArrayOutputStream();
            try {
                serializer.setOutput(os2, "UTF-8");
                serializer.startDocument("UTF-8", true);
                serializer.startTag("", AuthRequest.REQUEST_XML_KEY_ROUTE);
                serializer.startTag("", AuthRequest.REQUEST_XML_KEY_FNC_NO);
                serializer.text(FNC_DEC_NO_CT);
                serializer.endTag("", AuthRequest.REQUEST_XML_KEY_FNC_NO);
                serializer.startTag("", AuthRequest.REQUEST_XML_KEY_PRODUCT_ASPECT_KBN);
                serializer.text(PRODUCT_ASPECT_CLASS_PERIOD);
                serializer.endTag("", AuthRequest.REQUEST_XML_KEY_PRODUCT_ASPECT_KBN);
                serializer.startTag("", AuthRequest.REQUEST_XML_KEY_LINE_UP_CD);
                serializer.text(LINE_UP_CODE_ZM);
                serializer.endTag("", AuthRequest.REQUEST_XML_KEY_LINE_UP_CD);
                serializer.startTag("", AuthRequest.REQUEST_XML_KEY_DECISION_TYPE_KBN);
                serializer.text(TEXTBOOK_INFO);
                serializer.endTag("", AuthRequest.REQUEST_XML_KEY_DECISION_TYPE_KBN);
                String[] decisionTypes = {DEC_TYPE_D02, DEC_TYPE_D03, DEC_TYPE_D12, DEC_TYPE_Q01, DEC_TYPE_Q02};
                for (String decisionType : decisionTypes) {
                    serializer.startTag("", AuthRequest.REQUEST_XML_KEY_DECISION_TYPE_LIST);
                    serializer.startTag("", AuthRequest.REQUEST_XML_KEY_DECISION_TYPE);
                    serializer.text(decisionType);
                    serializer.endTag("", AuthRequest.REQUEST_XML_KEY_DECISION_TYPE);
                    serializer.endTag("", AuthRequest.REQUEST_XML_KEY_DECISION_TYPE_LIST);
                }
                serializer.startTag("", AuthRequest.REQUEST_XML_KEY_SERIAL_NUM);
                serializer.text(this.serial);
                serializer.endTag("", AuthRequest.REQUEST_XML_KEY_SERIAL_NUM);
                serializer.endTag("", AuthRequest.REQUEST_XML_KEY_ROUTE);
                serializer.endDocument();
                serializer.flush();
                this.xmlString = os2.toString();
                if (os2 != null) {
                    try {
                        os2.close();
                    } catch (IOException e) {
                    }
                }
            } catch (Exception e2) {
                os = os2;
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e3) {
                    }
                }
            } catch (Throwable th) {
                th = th;
                os = os2;
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e4) {
                    }
                }
                throw th;
            }
        } catch (Exception e5) {
        } catch (Throwable th2) {
            th = th2;
        }
    }

    @Override
    public void setPostParams(Map params) throws RemoteException {
        this.paramMap = params;
    }

    @Override
    public Map<String, String> getPostParams() throws RemoteException {
        return this.paramMap;
    }

    @Override
    public String getUri() throws RemoteException {
        return this.uriString;
    }

    @Override
    public String getHttpRequestBody() throws RemoteException {
        return this.xmlString;
    }

    @Override
    public void setHttpRequestBody(String reqString) throws RemoteException {
        this.xmlString = reqString;
    }

    @Override
    public void setRequestId(String id) throws RemoteException {
        this.reqId = id;
    }

    @Override
    public String getRequestId() throws RemoteException {
        return this.reqId;
    }
}
