package woohaengsi.qnadiary.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static woohaengsi.qnadiary.auth.oauth.type.ResourceServer.APPLE;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import woohaengsi.qnadiary.DatabaseCleanup;
import woohaengsi.qnadiary.answer.repository.AnswerRepository;
import woohaengsi.qnadiary.answer.service.AnswerService;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.domain.Question;
import woohaengsi.qnadiary.question.repository.QuestionRepository;

@ActiveProfiles("test")
@SpringBootTest
class MemberServiceTest {

    @Autowired
    DatabaseCleanup databaseCleanup;
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


    @AfterEach
    void tearDown() {
        databaseCleanup.afterPropertiesSet();
        databaseCleanup.execute();
    }

    @BeforeEach
    void setUp() {
        questionRepository.save(new Question("1번 질문"));
        questionRepository.save(new Question("2번 질문"));
        memberRepository.save(Member.of(APPLE, "resource_id", "name", "email", "image"));
    }

    @Test
    @DisplayName("30개의 질문에 답변하고 반복을 요청하면 다음 질문 번호는 1번이다.")
    void repeat_question_cycle_1() {
    	// given
        Member findMember = memberRepository.findById(1L).orElseThrow(IllegalArgumentException::new);
        for (int i=0; i < 30; i++) {
            findMember.increaseCurrentQuestionNumber();
        }
        findMember.repeatQuestionCycle();

    	// when
        Long curQuestionNumber = findMember.getCurrentQuestionNumber();

    	// then
        assertThat(curQuestionNumber).isEqualTo(1);
    }

    @Test
    @DisplayName("60개의 질문에 답변하고 반복을 요청하면 다음 질문 번호는 31번이다.")
    void repeat_question_cycle_2() {
        // given
        Member findMember = memberRepository.findById(1L).orElseThrow(IllegalArgumentException::new);
        for (int i=0; i < 60; i++) {
            findMember.increaseCurrentQuestionNumber();
        }
        findMember.repeatQuestionCycle();

        // when
        Long curQuestionNumber = findMember.getCurrentQuestionNumber();

        // then
        assertThat(curQuestionNumber).isEqualTo(31);
    }
}
