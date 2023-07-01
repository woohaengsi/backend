package woohaengsi.qnadiary.answer.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woohaengsi.qnadiary.answer.domain.Answer;
import woohaengsi.qnadiary.answer.dto.AnswersReadResponse;
import woohaengsi.qnadiary.answer.dto.AnswerCreateRequest;
import woohaengsi.qnadiary.answer.dto.AnswerDateByMonthResponse;
import woohaengsi.qnadiary.answer.dto.AnswerDateResponse;
import woohaengsi.qnadiary.answer.dto.AnswerDetailResponse;
import woohaengsi.qnadiary.answer.repository.AnswerRepository;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.domain.Question;
import woohaengsi.qnadiary.question.repository.QuestionRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;


    @Transactional
    public Map<String, Long> create(AnswerCreateRequest request, Long memberId) {
        Member findMember = findMemberBy(memberId);
        Question findQuestion = findQuestionBy(request.questionId());
        Answer createdAnswer = new Answer(findMember, findQuestion, request.content());
        answerRepository.save(createdAnswer);
        findMember.increaseCurrentQuestionNumber();
        return Collections.singletonMap("questionId", findQuestion.getId());
    }

    public AnswerDetailResponse findBy(Long answerId, Long memberId) {
        Member findMember = findMemberBy(memberId);
        Answer findAnswer = answerRepository.findByIdAndMember(answerId, findMember)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 답변입니다."));
        return AnswerDetailResponse.of(findAnswer);
    }
    @Transactional
    public void update(Long answerId, Long memberId, String content) {
        Member findMember = findMemberBy(memberId);
        Answer findAnswer = answerRepository.findByIdAndMember(answerId, findMember)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 답변입니다."));
        findAnswer.updateContent(content);
    }

    @Transactional
    public void delete(Long answerId, Long memberId) {
        Member findMember = findMemberBy(memberId);
        Answer findAnswer = answerRepository.findByIdAndMember(answerId, findMember)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 답변입니다."));
        answerRepository.delete(findAnswer);
    }
    public AnswerDateByMonthResponse findAnswerDateByMonth(Long memberId, Integer year, Integer month) {
        Member findMember = findMemberBy(memberId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        List<Answer> findAnswers = answerRepository.findAllByMemberAndCreatedAtBetween(findMember, start, end);
        return answersToDateByMonthResponse(findAnswers);
    }

    public AnswersReadResponse findAnswersByQuestion(Long memberId, Long questionId) {
        Member findMember = findMemberBy(memberId);
        Question findQuestion = findQuestionBy(questionId);
        List<Answer> findAnswers = answerRepository.findAllByMemberAndQuestion(
            findMember, findQuestion);
        return answersToReadResponse(findAnswers);
    }

    private AnswerDateByMonthResponse answersToDateByMonthResponse(List<Answer> answers) {
        return AnswerDateByMonthResponse.of(answers.stream()
            .map(AnswerDateResponse::of)
            .sorted(Comparator.comparing(AnswerDateResponse::getCreatedAt).reversed())
            .collect(Collectors.toList()));
    }

    private AnswersReadResponse answersToReadResponse(List<Answer> answers) {
        return AnswersReadResponse.of(answers.stream()
            .map(AnswerDetailResponse::of)
            .sorted(Comparator.comparing(AnswerDetailResponse::getCreatedAt).reversed())
            .collect(Collectors.toList()));
    }

    private Member findMemberBy(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }

    private Question findQuestionBy(Long questionId) {
        return questionRepository.findById(questionId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 질문입니다."));
    }

}
