package woohaengsi.qnadiary.answer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static woohaengsi.qnadiary.auth.oauth.type.ResourceServer.APPLE;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import woohaengsi.qnadiary.DatabaseCleanup;
import woohaengsi.qnadiary.answer.domain.Answer;
import woohaengsi.qnadiary.answer.dto.AnswerDetailResponse;
import woohaengsi.qnadiary.answer.repository.AnswerRepository;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.domain.Question;
import woohaengsi.qnadiary.question.repository.QuestionRepository;

@ActiveProfiles("test")
@SpringBootTest
class AnswerFindByQuestionTest {

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

    @BeforeEach
    void setUp() {
        questionRepository.save(new Question("1번 질문"));
        questionRepository.save(new Question("2번 질문"));
        memberRepository.save(Member.of(APPLE, "resource_id", "name", "email", "image"));
    }

    @Test
    @DisplayName("특정 질문을 입력하면 해당 질문에 대한 답변을 반환한다.")
    void find_answers_by_month() {
    	// given
        Member findMember = memberRepository.findById(1L).orElseThrow(IllegalArgumentException::new);
        Question question = questionRepository.findById(1L)
            .orElseThrow(IllegalArgumentException::new);
        Answer answer1 = new Answer(findMember, question, "답변 1");
        Answer answer2 = new Answer(findMember, question, "답변 2");
        answerRepository.saveAll(List.of(answer1,  answer2));

        // when
        List<AnswerDetailResponse> responses = answerService.findAnswersByQuestion(
            findMember.getId(), question.getId()).getResponses();

        // then
        assertThat(responses).hasSize(2)
            .extracting("id", "question", "answer")
            .containsExactly(
                tuple(2L, question.getContent(), answer2.getContent()),
                tuple(1L, question.getContent(), answer1.getContent())
            );
    }
}
