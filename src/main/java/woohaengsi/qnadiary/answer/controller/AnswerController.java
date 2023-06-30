package woohaengsi.qnadiary.answer.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import woohaengsi.qnadiary.answer.dto.AnswerCreateRequest;
import woohaengsi.qnadiary.answer.service.AnswerService;
import woohaengsi.qnadiary.auth.jwt.JwtProvider;

@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final JwtProvider jwtProvider;

    @PostMapping("/answer")
    public void create(@RequestHeader("Authorization") String accessToken, @RequestBody AnswerCreateRequest request) {
        Long memberId = jwtProvider.decode(accessToken);
        answerService.create(request, memberId);
    }
}
