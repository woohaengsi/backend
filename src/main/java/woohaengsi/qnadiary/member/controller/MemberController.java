package woohaengsi.qnadiary.member.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RestController;
import woohaengsi.qnadiary.auth.jwt.JwtProvider;
import woohaengsi.qnadiary.member.service.MemberService;

@Tag(name = "member", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "질문 반복", description = "기존 설정된 질문세트 크기만큼 질문이 다시 제공된다.")
    @PatchMapping("/member/question/repeat")
    public void repeatQuestionCycle(HttpServletRequest httpRequest) {
        String accessToken = getAuthorization(httpRequest);
        Long memberId = decodeAccessToken(accessToken);
        memberService.repeatQuestionCycle(memberId);
    }

    private Long decodeAccessToken(String accessToken) {
        return jwtProvider.decode(accessToken);
    }

    private String getAuthorization(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
