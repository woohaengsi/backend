package woohaengsi.qnadiary.question.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import woohaengsi.qnadiary.auth.jwt.JwtProvider;
import woohaengsi.qnadiary.question.dto.QuestionReadResponse;
import woohaengsi.qnadiary.question.service.QuestionService;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;
    private final JwtProvider jwtProvider;

    @GetMapping("/question")
    public QuestionReadResponse find(@RequestHeader("Authorization") String accessToken) {
        Long memberId = decodeAccessToken(accessToken);
        return questionService.findBy(memberId);
    }

    private Long decodeAccessToken(String accessToken) {
        return jwtProvider.decode(accessToken);
    }
}
