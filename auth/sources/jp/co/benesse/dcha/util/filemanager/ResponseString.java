package jp.co.benesse.dcha.util.filemanager;

public class ResponseString {
    public static final int COMPLETE = 0;
    public static final int ERROR = -1;
    private String messageCode;
    private int resultCode = -1;
    private String resultString;

    public int getResultCode() {
        return this.resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMessageCode() {
        return this.messageCode;
    }

    public void setMessageCode(String messageCode) {
        this.messageCode = messageCode;
    }

    public String getResultString() {
        return this.resultString;
    }

    public void setResultString(String resultString) {
        this.resultString = resultString;
    }
}
