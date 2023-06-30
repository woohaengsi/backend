package woohaengsi.qnadiary.auth.oauth.properties;

public record OAuthProperties (
    String callbackUrl, String clientId, String clientSecret,
    String accessTokenApiUrl, String loginFormUrl, String userInfoApiUrl, String grantType, String redirectUri) {

}
