package woohaengsi.qnadiary.answer.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woohaengsi.qnadiary.answer.domain.Answer;
import woohaengsi.qnadiary.answer.dto.AnswerCreateRequest;
import woohaengsi.qnadiary.answer.dto.AnswerDateByMonthResponse;
import woohaengsi.qnadiary.answer.dto.AnswerDateResponse;
import woohaengsi.qnadiary.answer.dto.AnswerDetailResponse;
import woohaengsi.qnadiary.answer.dto.AnswersReadResponse;
import woohaengsi.qnadiary.answer.repository.AnswerRepository;
import woohaengsi.qnadiary.bloomedflower.service.BloomedFlowerService;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.domain.Question;
import woohaengsi.qnadiary.question.repository.QuestionRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnswerService {

    private final BloomedFlowerService bloomedFlowerService;
    private final AnswerRepository answerRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;


    @Transactional
    public void create(AnswerCreateRequest request, Long memberId) {
        Member findMember = findMemberBy(memberId);
        Question findQuestion = findQuestionBy(request.getQuestionId());
        Answer createdAnswer = new Answer(findMember, findQuestion, request.getContent());
        answerRepository.save(createdAnswer);
        if (findMember.isAbleToGetFlower()) {
            bloomedFlowerService.giveFlowerToMember(findMember);
        }
        findMember.increaseCurrentQuestionNumber();
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
    public AnswerDateByMonthResponse findAnswerDateByYearAndMonth(Long memberId, Integer year, Integer month) {
        List<Answer> findAnswers = getAnswersByYearMonth(memberId, year, month);
        return answersToDateByMonthResponse(findAnswers);
    }

    public AnswersReadResponse findAnswersByQuestion(Long memberId, Long questionId) {
        Member findMember = findMemberBy(memberId);
        Question findQuestion = findQuestionBy(questionId);
        List<Answer> findAnswers = answerRepository.findAllByMemberAndQuestion(
            findMember, findQuestion);
        return answersToReadResponse(findAnswers);
    }

    public AnswersReadResponse findAnswerByYearAndMonth(Long memberId, Integer year, Integer month) {
        List<Answer> findAnswers = getAnswersByYearMonth(memberId, year, month);
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
    private List<Answer> getAnswersByYearMonth(Long memberId, Integer year, Integer month) {
        Member findMember = findMemberBy(memberId);
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
        return answerRepository.findAllByMemberAndCreatedAtBetween(findMember, start, end);
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
