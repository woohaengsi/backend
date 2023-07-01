package woohaengsi.qnadiary.answer.repository;

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
import woohaengsi.qnadiary.answer.domain.Answer;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.domain.Question;
import woohaengsi.qnadiary.question.repository.QuestionRepository;

@ActiveProfiles("test")
@SpringBootTest
class AnswerRepositoryTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;

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
    @DisplayName("답변을 생성하면 저장된 답변의 갯수가 증가한다")
    void create_increase_size() {
    	// given
        Member member = memberRepository.findById(1L).orElseThrow(IllegalArgumentException::new);
        Question question = questionRepository.findById(1L)
            .orElseThrow(IllegalArgumentException::new);
        String content = "답변1";
        Answer answer = new Answer(member, question, content);
        int oldSize = answerRepository.findAll().size();
        memberRepository.save(member);
        questionRepository.save(question);

        // when
        answerRepository.save(answer);
        int newSize = answerRepository.findAll().size();

        // then
        assertThat(newSize).isEqualTo(oldSize + 1);
    }
}
