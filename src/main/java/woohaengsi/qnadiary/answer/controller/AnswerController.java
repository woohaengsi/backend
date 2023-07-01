package woohaengsi.qnadiary.answer.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import woohaengsi.qnadiary.answer.dto.AnswerCreateRequest;
import woohaengsi.qnadiary.answer.dto.AnswerDateByMonthResponse;
import woohaengsi.qnadiary.answer.dto.AnswerDetailResponse;
import woohaengsi.qnadiary.answer.service.AnswerService;
import woohaengsi.qnadiary.auth.jwt.JwtProvider;

@RestController
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;
    private final JwtProvider jwtProvider;

    @PostMapping("/answer")
    public Map<String, Long> create(@RequestHeader("Authorization") String accessToken, @RequestBody AnswerCreateRequest request) {
        Long memberId = decodeAccessToken(accessToken);
        return answerService.create(request, memberId);
    }

    @GetMapping("/answers/{id}")
    public AnswerDetailResponse findById(@RequestHeader("Authorization") String accessToken, @PathVariable("id") Long id) {
        Long memberId = decodeAccessToken(accessToken);
        return answerService.findBy(id, memberId);
    }

    @PatchMapping("/answers/{id}")
    public void updateAnswer(@RequestHeader("Authorization") String accessToken,
        @PathVariable("id") Long id, @RequestBody String content) {
        Long memberId = decodeAccessToken(accessToken);
        answerService.update(id, memberId, content);
    }

    @DeleteMapping("/answers/{id}")
    public void deleteAnswer(@RequestHeader("Authorization") String accessToken,
        @PathVariable("id") Long id) {
        Long memberId = decodeAccessToken(accessToken);
        answerService.delete(id, memberId);
    }

    @GetMapping("/answers/days/{year}/{month}")
    public AnswerDateByMonthResponse findDateByMonth(@RequestHeader("Authorization") String accessToken, @PathVariable("year") Integer year, @PathVariable("month") Integer month) {
        Long memberId = decodeAccessToken(accessToken);
        return answerService.findAnswerDateByMonth(memberId, year, month);
    }

    private Long decodeAccessToken(String accessToken) {
        return jwtProvider.decode(accessToken);
    }
}
