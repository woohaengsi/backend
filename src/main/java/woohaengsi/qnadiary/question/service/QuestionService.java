package woohaengsi.qnadiary.question.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.domain.Question;
import woohaengsi.qnadiary.question.dto.QuestionReadResponse;
import woohaengsi.qnadiary.question.repository.QuestionRepository;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public QuestionReadResponse findBy(Long memberId) {
        Member findMember = findMemberBy(memberId);
        Question findQuestion = questionRepository.findById(findMember.getCurrentQuestionNumber())
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 질문입니다."));
        return QuestionReadResponse.of(findQuestion);
    }

    private Member findMemberBy(Long memberId) {
        return memberRepository.findById(memberId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));
    }
}
