package woohaengsi.qnadiary.question.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import woohaengsi.qnadiary.auth.jwt.JwtProvider;
import woohaengsi.qnadiary.question.dto.QuestionReadResponse;
import woohaengsi.qnadiary.question.service.QuestionService;

@Tag(name = "question", description = "질문 관련 API")
@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final JwtProvider jwtProvider;

    @Operation(summary = "질문을 조회", description = "회원이 답변할 차례의 질문을 조회한다.")
    @GetMapping("/question")
    public QuestionReadResponse find(HttpServletRequest httpRequest) {
        String accessToken = getAuthorization(httpRequest);
        Long memberId = decodeAccessToken(accessToken);
        return questionService.findBy(memberId);
    }

    private Long decodeAccessToken(String accessToken) {
        return jwtProvider.decode(accessToken);
    }

    private String getAuthorization(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
