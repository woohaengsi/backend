package woohaengsi.qnadiary.answer.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    @PostMapping("/answer")
    public void create(@RequestHeader("Authorization") String accessToken, @RequestBody AnswerCreateRequest request) {
        Long memberId = decodeAccessToken(accessToken);
        answerService.create(request, memberId);
    }

    @Operation(summary = "답변 상세 조회", description = "답변을 상세 조회합니다.")
    @GetMapping("/answers/{id}")
    public AnswerDetailResponse findById(@RequestHeader("Authorization") String accessToken, @PathVariable("id") Long id) {
        Long memberId = decodeAccessToken(accessToken);
        return answerService.findBy(id, memberId);
    }

    @Operation(summary = "답변 수정", description = "답변을 수정합니다.")
    @PatchMapping("/answers/{id}")
    public void updateAnswer(@RequestHeader("Authorization") String accessToken,
        @PathVariable("id") Long id, @RequestBody String content) {
        Long memberId = decodeAccessToken(accessToken);
        answerService.update(id, memberId, content);
    }

    @Operation(summary = "답변 삭제", description = "답변을 삭제합니다.")
    @DeleteMapping("/answers/{id}")
    public void deleteAnswer(@RequestHeader("Authorization") String accessToken,
        @PathVariable("id") Long id) {
        Long memberId = decodeAccessToken(accessToken);
        answerService.delete(id, memberId);
    }

    @Operation(summary = "답변 작성일 조회", description = "해당 연,월의 답변 작성일을 조회합니다.")
    @GetMapping("/answers/days/{year}/{month}")
    public AnswerDateByMonthResponse findDateByYearAndMonth(@RequestHeader("Authorization") String accessToken, @PathVariable("year") Integer year, @PathVariable("month") Integer month) {
        Long memberId = decodeAccessToken(accessToken);
        return answerService.findAnswerDateByYearAndMonth(memberId, year, month);
    }

    @Operation(summary = "특정 질문에 대한 답변 조회", description = "해당 질문에 작성한 답변을 조회합니다.")
    @GetMapping("/answers/question/{questionId}")
    public AnswersReadResponse findByQuestion(@RequestHeader("Authorization") String accessToken, @PathVariable("questionId") Long questionId) {
        Long memberId = decodeAccessToken(accessToken);
        return answerService.findAnswersByQuestion(memberId, questionId);
    }

    @Operation(summary = "특정 연,월에 작성한 답변 조회", description = "해당 기간에 작성한 답변을 조회합니다.")
    @GetMapping("/answers/{year}/{month}")
    public AnswersReadResponse findByYearAndMonth(@RequestHeader("Authorization") String accessToken, @PathVariable("year") Integer year, @PathVariable("month") Integer month) {
        Long memberId = decodeAccessToken(accessToken);
        return answerService.findAnswerByYearAndMonth(memberId, year, month);
    }

    private Long decodeAccessToken(String accessToken) {
        return jwtProvider.decode(accessToken);
    }
}
