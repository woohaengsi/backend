package woohaengsi.qnadiary.auth.oauth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AccessTokenResponse {

    private String tokenType;
    private String accessToken;

    public OAuthAccessToken toToken() {
        return new OAuthAccessToken(accessToken, tokenType);
    }
}
