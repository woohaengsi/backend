package woohaengsi.qnadiary.question.service;

import static woohaengsi.qnadiary.auth.oauth.type.ResourceServer.APPLE;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import woohaengsi.qnadiary.answer.dto.AnswerCreateRequest;
import woohaengsi.qnadiary.answer.service.AnswerService;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.domain.Question;
import woohaengsi.qnadiary.question.dto.QuestionReadResponse;
import woohaengsi.qnadiary.question.repository.QuestionRepository;
import woohaengsi.qnadiary.ServiceIntegrationTest;

@Sql("classpath:/data.sql")
@ServiceIntegrationTest
class QuestionServiceTest {

    @Autowired
    QuestionService questionService;
    @Autowired
    AnswerService answerService;
    @Autowired
    QuestionRepository questionRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("현재 순서의 질문을 조회한다.")
    void find_now_question() {
    	// given
        Member member = memberRepository.save(
            Member.of(APPLE, "resource_id", "name", "email", "image"));

        Long curQuestionId = 1L;
        Question question = questionRepository.findById(curQuestionId).get();

        // when
        QuestionReadResponse findQuestion = questionService.findBy(member.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(member.getCurrentQuestionNumber()).isEqualTo(question.getId());
            softAssertions.assertThat(findQuestion.getId()).isEqualTo(question.getId());
            softAssertions.assertThat(findQuestion.getContent()).isEqualTo(question.getContent());
        });
    }

    @Test
    @DisplayName("한번 질문을 답변하면 다음 질문을 조회한다.")
    void find_next_question() {
        // given
        Member member = memberRepository.save(
            Member.of(APPLE, "resource_id", "name", "email", "image"));

        Long curQuestionId = 1L;
        Long nextQuestionId = curQuestionId + 1;

        answerService.create(new AnswerCreateRequest(curQuestionId, "대답"), member.getId());

        Question question = questionRepository.findById(nextQuestionId).get();

        // when
        QuestionReadResponse findQuestion = questionService.findBy(member.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
                softAssertions.assertThat(member.getCurrentQuestionNumber()).isEqualTo(question.getId());
                softAssertions.assertThat(findQuestion.getId()).isEqualTo(question.getId());
                softAssertions.assertThat(findQuestion.getContent()).isEqualTo(question.getContent());
            }
        );
    }
}
