package woohaengsi.qnadiary.question.service;

import static woohaengsi.qnadiary.auth.oauth.type.ResourceServer.APPLE;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import woohaengsi.qnadiary.DatabaseCleanup;
import woohaengsi.qnadiary.answer.dto.AnswerCreateRequest;
import woohaengsi.qnadiary.answer.service.AnswerService;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.domain.Question;
import woohaengsi.qnadiary.question.dto.QuestionReadResponse;
import woohaengsi.qnadiary.question.repository.QuestionRepository;

@ActiveProfiles("test")
@SpringBootTest
class QuestionServiceTest {

    @Autowired
    DatabaseCleanup databaseCleanup;
    @Autowired
    QuestionService questionService;
    @Autowired
    AnswerService answerService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        questionRepository.save(new Question("1번 질문"));
        questionRepository.save(new Question("2번 질문"));
        memberRepository.save(Member.of(APPLE, "resource_id", "name", "email", "image"));
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.afterPropertiesSet();
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("현재 순서의 질문을 조회한다.")
    void find_now_question() {
    	// given
        Member findMember = memberRepository.findById(1L).orElseThrow(IllegalArgumentException::new);
        Question question1 = questionRepository.findById(1L)
            .orElseThrow(IllegalArgumentException::new);
        // when
        QuestionReadResponse findQuestion = questionService.findBy(findMember.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(findMember.getCurrentQuestionNumber()).isEqualTo(question1.getId());
            softAssertions.assertThat(findQuestion.getId()).isEqualTo(question1.getId());
            softAssertions.assertThat(findQuestion.getContent()).isEqualTo(question1.getContent());
        });
    }

    @Test
    @DisplayName("한번 질문을 답변하면 다음 질문을 조회한다.")
    void find_next_question() {
        // given
        Question question2 = questionRepository.findById(2L)
            .orElseThrow(IllegalArgumentException::new);
        answerService.create(new AnswerCreateRequest(1L, "대답"), 1L);
        Member findMember = memberRepository.findById(1L).orElseThrow(IllegalArgumentException::new);
        // when
        QuestionReadResponse findQuestion = questionService.findBy(findMember.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(findMember.getCurrentQuestionNumber()).isEqualTo(question2.getId());
                softAssertions.assertThat(findQuestion.getId()).isEqualTo(question2.getId());
                softAssertions.assertThat(findQuestion.getContent()).isEqualTo(question2.getContent());
            }
        );
    }
}
