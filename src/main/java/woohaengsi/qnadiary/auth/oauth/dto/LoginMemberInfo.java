package woohaengsi.qnadiary.auth.oauth.dto;

import lombok.Builder;
import lombok.Getter;
import woohaengsi.qnadiary.member.domain.Member;

@Getter
public class LoginMemberInfo {

    private final String name;
    private final String profileImageUrl;
    private final String emailAddress;

    @Builder
    private LoginMemberInfo(String nickname, String profileImageUrl, String emailAddress) {
        this.name = nickname;
        this.profileImageUrl = profileImageUrl;
        this.emailAddress = emailAddress;
    }

    public static LoginMemberInfo of(Member member) {
        return LoginMemberInfo.builder()
            .nickname(member.getNickname())
            .profileImageUrl(member.getProfileImageUrl())
            .emailAddress(member.getEmailAddress())
            .build();
    }
}
