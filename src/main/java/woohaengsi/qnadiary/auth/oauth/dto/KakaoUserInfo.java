package woohaengsi.qnadiary.auth.oauth.dto;

import static woohaengsi.qnadiary.auth.oauth.type.ResourceServer.KAKAO;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import woohaengsi.qnadiary.member.domain.Member;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserInfo {

    private String id;
    private Properties properties;
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Properties {

        private String nickname;
        private String profileImage;
        private String thumbnailImage;
    }

    @Getter
    public static class KakaoAccount {
        private String email;
    }

    public Member toMember() {
        return Member.of(KAKAO, id, properties.nickname, kakaoAccount.email, properties.profileImage);
    }
}
