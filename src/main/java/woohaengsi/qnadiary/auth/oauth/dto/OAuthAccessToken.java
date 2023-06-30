package woohaengsi.qnadiary.auth.oauth.dto;

public record OAuthAccessToken(String tokenValue, String tokenType) {

    public String createAuthorizationValue() {
        return this.tokenType + " " + this.tokenValue;
    }
}
