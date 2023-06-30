package woohaengsi.qnadiary.answer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woohaengsi.qnadiary.answer.domain.Answer;
import woohaengsi.qnadiary.answer.dto.AnswerCreateRequest;
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
    public void create(AnswerCreateRequest request, Long memberId) {
        Member findMember = findMemberBy(memberId);
        Question findQuestion = findQuestionBy(request.questionId());
        Answer createdAnswer = new Answer(findMember, findQuestion, request.content());
        answerRepository.save(createdAnswer);
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
