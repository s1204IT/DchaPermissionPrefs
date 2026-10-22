package jp.co.benesse.touch.auth;

import jp.co.benesse.touch.util.UrlUtil;

public class OnetimekeyRequest extends AuthServerRequest {
    public OnetimekeyRequest() {
        super(UrlUtil.ONETIMEKEY_API);
    }
}
