package woohaengsi.qnadiary.answer.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import woohaengsi.qnadiary.DatabaseCleanup;
import woohaengsi.qnadiary.answer.domain.Answer;
import woohaengsi.qnadiary.answer.dto.AnswerCreateRequest;
import woohaengsi.qnadiary.answer.repository.AnswerRepository;
import woohaengsi.qnadiary.auth.oauth.type.ResourceServer;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.domain.Question;
import woohaengsi.qnadiary.question.repository.QuestionRepository;

@ActiveProfiles("test")
@SpringBootTest
class AnswerServiceTest {

    @Autowired
    DatabaseCleanup databaseCleanup;
    @Autowired
    AnswerService answerService;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    QuestionRepository questionRepository;

    @AfterEach
    void tearDown() {
        databaseCleanup.afterPropertiesSet();
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("답변을 저장한다.")
    void create_answer() {
    	// given
        Member member = Member.of(ResourceServer.KAKAO, "TEST", "TEST", "TEST", "TEST");
        Question question = new Question("질문1");

        memberRepository.save(member);
        questionRepository.save(question);

        AnswerCreateRequest request = new AnswerCreateRequest(1L, "답변1");

    	// when
        answerService.create(request, 1L);
        List<Answer> responses = answerRepository.findAll();

        // then
        assertThat(responses).hasSize(1);
    }

}
