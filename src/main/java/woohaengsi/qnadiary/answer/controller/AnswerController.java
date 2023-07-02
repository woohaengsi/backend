package woohaengsi.qnadiary.answer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import woohaengsi.qnadiary.answer.dto.AnswerCreateRequest;
import woohaengsi.qnadiary.answer.dto.AnswerDateByMonthResponse;
import woohaengsi.qnadiary.answer.dto.AnswerDetailResponse;
import woohaengsi.qnadiary.answer.dto.AnswersReadResponse;
import woohaengsi.qnadiary.answer.service.AnswerService;
import woohaengsi.qnadiary.auth.jwt.JwtProvider;

@Tag(name = "answer", description = "답변 관련 API")
@RequiredArgsConstructor
@RestController
public class AnswerController {

    private final AnswerService answerService;
    private final JwtProvider jwtProvider;


    @Operation(summary = "답변 생성", description = "질문에 대한 답변을 등록합니다.")
    @PostMapping(path = "/answer")
    public void create(HttpServletRequest httpRequest, @RequestBody AnswerCreateRequest request) {
        String accessToken = getAuthorization(httpRequest);
        Long memberId = decodeAccessToken(accessToken);
        answerService.create(request, memberId);
    }

    @Operation(summary = "답변 상세 조회", description = "답변을 상세 조회합니다.")
    @GetMapping("/answers/{id}")
    public AnswerDetailResponse findById(HttpServletRequest httpRequest, @PathVariable("id") Long id) {
        String accessToken = getAuthorization(httpRequest);
        Long memberId = decodeAccessToken(accessToken);
        return answerService.findBy(id, memberId);
    }

    @Operation(summary = "답변 수정", description = "답변을 수정합니다.")
    @PatchMapping("/answers/{id}")
    public void updateAnswer(HttpServletRequest httpRequest,
        @PathVariable("id") Long id, @RequestBody String content) {
        String accessToken = getAuthorization(httpRequest);
        Long memberId = decodeAccessToken(accessToken);
        answerService.update(id, memberId, content);
    }

    @Operation(summary = "답변 삭제", description = "답변을 삭제합니다.")
    @DeleteMapping("/answers/{id}")
    public void deleteAnswer(HttpServletRequest httpRequest,
        @PathVariable("id") Long id) {
        String accessToken = getAuthorization(httpRequest);
        Long memberId = decodeAccessToken(accessToken);
        answerService.delete(id, memberId);
    }

    @Operation(summary = "답변 작성일 조회", description = "해당 연,월의 답변 작성일을 조회합니다.")
    @GetMapping("/answers/days/{year}/{month}")
    public AnswerDateByMonthResponse findDateByYearAndMonth(HttpServletRequest httpRequest, @PathVariable("year") Integer year, @PathVariable("month") Integer month) {
        String accessToken = getAuthorization(httpRequest);
        Long memberId = decodeAccessToken(accessToken);
        return answerService.findAnswerDateByYearAndMonth(memberId, year, month);
    }

    @Operation(summary = "특정 질문에 대한 답변 조회", description = "해당 질문에 작성한 답변을 조회합니다.")
    @GetMapping("/answers/question/{questionId}")
    public AnswersReadResponse findByQuestion(HttpServletRequest httpRequest, @PathVariable("questionId") Long questionId) {
        String accessToken = getAuthorization(httpRequest);
        Long memberId = decodeAccessToken(accessToken);
        return answerService.findAnswersByQuestion(memberId, questionId);
    }

    @Operation(summary = "특정 연,월에 작성한 답변 조회", description = "해당 기간에 작성한 답변을 조회합니다.")
    @GetMapping("/answers/{year}/{month}")
    public AnswersReadResponse findByYearAndMonth(HttpServletRequest httpRequest, @PathVariable("year") Integer year, @PathVariable("month") Integer month) {
        String accessToken = getAuthorization(httpRequest);
        Long memberId = decodeAccessToken(accessToken);
        return answerService.findAnswerByYearAndMonth(memberId, year, month);
    }

    private Long decodeAccessToken(String accessToken) {
        return jwtProvider.decode(accessToken);
    }
    private String getAuthorization(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
