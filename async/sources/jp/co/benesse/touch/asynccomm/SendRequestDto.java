package jp.co.benesse.touch.asynccomm;

public class SendRequestDto {
    private static final String TAG = "SendRequestDto";
    private String api;
    private Integer id;
    private boolean replaceFlag;
    private String request;
    private String url;

    public SendRequestDto(String str, String str2, String str3, boolean z) {
        this.url = str;
        this.api = str2;
        this.request = str3;
        this.replaceFlag = z;
    }

    public SendRequestDto(int i, String str, String str2, String str3, int i2) {
        this.id = Integer.valueOf(i);
        this.url = str;
        this.api = str2;
        this.request = str3;
        if (i2 > 0) {
            this.replaceFlag = true;
        } else {
            this.replaceFlag = false;
        }
    }

    public Integer getId() {
        return this.id;
    }

    public String getUrl() {
        return this.url;
    }

    public String getApi() {
        return this.api;
    }

    public boolean isReplaceFlag() {
        return this.replaceFlag;
    }

    public String getRequest() {
        return this.request;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("SendRequest[ ");
        sb.append("id:");
        Integer num = this.id;
        if (num != null) {
            sb.append(num);
        } else {
            sb.append("null");
        }
        sb.append(" , url:");
        sb.append(this.url);
        sb.append(" , api:");
        sb.append(this.api);
        sb.append(" , request:");
        sb.append(this.request);
        sb.append(" , replaceFlag:");
        sb.append(this.replaceFlag);
        sb.append(" ]");
        return sb.toString();
    }
}
