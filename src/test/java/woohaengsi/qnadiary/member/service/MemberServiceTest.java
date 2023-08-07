package woohaengsi.qnadiary.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static woohaengsi.qnadiary.auth.oauth.type.ResourceServer.APPLE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import woohaengsi.qnadiary.answer.repository.AnswerRepository;
import woohaengsi.qnadiary.answer.service.AnswerService;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.repository.QuestionRepository;
import woohaengsi.qnadiary.ServiceIntegrationTest;

@ServiceIntegrationTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    AnswerService answerService;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    QuestionRepository questionRepository;

    @Test
    @DisplayName("30개의 질문에 답변하고 반복을 요청하면 다음 질문 번호는 1번이다.")
    void repeat_question_cycle_1() {
    	// given
        Member member = memberRepository.save(
            Member.of(APPLE, "resource_id", "name", "email", "image"));

        for (int i=0; i < 30; i++) {
            member.increaseCurrentQuestionNumber();
        }

        member.repeatQuestionCycle();

    	// when
        Long curQuestionNumber = member.getCurrentQuestionNumber();

    	// then
        assertThat(curQuestionNumber).isEqualTo(1);
    }

    @Test
    @DisplayName("60개의 질문에 답변하고 반복을 요청하면 다음 질문 번호는 31번이다.")
    void repeat_question_cycle_2() {
        // given
        Member member = memberRepository.save(
            Member.of(APPLE, "resource_id", "name", "email", "image"));

        for (int i=0; i < 60; i++) {
            member.increaseCurrentQuestionNumber();
        }

        member.repeatQuestionCycle();

        // when
        Long curQuestionNumber = member.getCurrentQuestionNumber();

        // then
        assertThat(curQuestionNumber).isEqualTo(31);
    }
}
