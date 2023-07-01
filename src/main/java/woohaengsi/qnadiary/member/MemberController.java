package woohaengsi.qnadiary.member;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import woohaengsi.qnadiary.auth.jwt.JwtProvider;
import woohaengsi.qnadiary.member.service.MemberService;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @PatchMapping("/member/question/repeat")
    public void repeatQuestionCycle(@RequestHeader("Authorization") String accessToken) {
        Long memberId = decodeAccessToken(accessToken);
        memberService.repeatQuestionCycle(memberId);
    }

    private Long decodeAccessToken(String accessToken) {
        return jwtProvider.decode(accessToken);
    }
}
