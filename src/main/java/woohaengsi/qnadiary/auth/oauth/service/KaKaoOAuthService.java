package woohaengsi.qnadiary.auth.oauth.service;

import java.net.URI;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import woohaengsi.qnadiary.auth.oauth.dto.OAuthAccessToken;
import woohaengsi.qnadiary.auth.oauth.properties.OAuthProperties;
import woohaengsi.qnadiary.auth.oauth.properties.OAuthPropertiesMapper;
import woohaengsi.qnadiary.auth.oauth.service.feignclient.KakaoOAuthApiClient;
import woohaengsi.qnadiary.member.domain.Member;

@Service("kakao")
public class KaKaoOAuthService implements OAuthService {

    private final KakaoOAuthApiClient apiClient;
    private final OAuthProperties oAuthProperties;

    public KaKaoOAuthService(KakaoOAuthApiClient apiClient, OAuthPropertiesMapper oAuthPropertiesMapper) {
        this.apiClient = apiClient;
        this.oAuthProperties = oAuthPropertiesMapper.getOAuthProperties("kakao");
    }

    @Override
    public OAuthAccessToken requestAccessToken(String authorizationCode) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", oAuthProperties.grantType());
        body.add("client_id", oAuthProperties.clientId());
        body.add("client_secret", oAuthProperties.clientSecret());
        body.add("redirect_uri", oAuthProperties.redirectUri());
        body.add("code", authorizationCode);
        URI tokenUrl = URI.create(oAuthProperties.accessTokenApiUrl());
        return apiClient.requestAccessToken(tokenUrl, body).toToken();
    }

    @Override
    public Member requestUserInfo(OAuthAccessToken oAuthAccessToken) {
        URI userInfoUrl = URI.create(oAuthProperties.userInfoApiUrl());
        String authorizationValue = oAuthAccessToken.createAuthorizationValue();
        return apiClient.requestUserInfo(userInfoUrl, authorizationValue).toMember();
    }
}
