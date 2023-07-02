package woohaengsi.qnadiary.auth.oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woohaengsi.qnadiary.auth.oauth.type.ResourceServer;
import woohaengsi.qnadiary.member.domain.Member;

@Schema(description = "로그인 유저 정보")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    @Schema(description = "이름")
    private String nickname;
    @Schema(description = "이메일 주소")
    private String email;
    @Schema(description = "프로필 이미지 URL")
    private String profileImageUrl;

    public LoginRequest(String nickname, String email, String profileImageUrl) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public Member toMember(ResourceServer resourceServer, String resourceServerId) {
        return Member.of(resourceServer, resourceServerId, nickname, email, profileImageUrl);
    }
}
