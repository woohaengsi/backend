package woohaengsi.qnadiary.answer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import woohaengsi.qnadiary.answer.domain.Answer;
import woohaengsi.qnadiary.auth.oauth.type.ResourceServer;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.domain.Question;
import woohaengsi.qnadiary.question.repository.QuestionRepository;

@DataJpaTest
class AnswerRepositoryTest {

    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @Test
    @DisplayName("답변을 생성하면 저장된 답변의 갯수가 증가한다")
    void create_increase_size() {
    	// given
        Member member = Member.of(ResourceServer.KAKAO, "TEST", "TEST", "TEST", "TEST");
        Question question = new Question("질문1");
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
