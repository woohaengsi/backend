package woohaengsi.qnadiary.auth.oauth.service;

import woohaengsi.qnadiary.auth.oauth.dto.OAuthAccessToken;
import woohaengsi.qnadiary.member.domain.Member;

public interface OAuthService {

    OAuthAccessToken requestAccessToken(String authorizationCode);

    Member requestUserInfo(OAuthAccessToken oAuthAccessToken);
}
