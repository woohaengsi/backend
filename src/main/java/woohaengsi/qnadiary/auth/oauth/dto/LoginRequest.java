package woohaengsi.qnadiary.auth.oauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import woohaengsi.qnadiary.auth.oauth.type.ResourceServer;
import woohaengsi.qnadiary.member.domain.Member;

@Getter
@Schema(description = "로그인 유저 정보")
public class LoginRequest {

    @Schema(description = "이름")
    private final String nickname;
    @Schema(description = "이메일 주소")
    private final String email;
    @Schema(description = "프로필 이미지 URL")
    private final String profileImageUrl;

    public LoginRequest(String nickname, String email, String profileImageUrl) {
        this.nickname = nickname;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
    }

    public Member toMember(ResourceServer resourceServer, String resourceServerId) {
        return Member.of(resourceServer, resourceServerId, nickname, email, profileImageUrl);
    }
}
