package jp.co.benesse.touch.auth;

import jp.co.benesse.touch.util.UrlUtil;

public class SessionUpdateRequest extends AuthServerRequest {
    public SessionUpdateRequest() {
        super(UrlUtil.SESSION_UPDATE_API);
    }
}
