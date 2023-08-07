package woohaengsi.qnadiary.answer.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static woohaengsi.qnadiary.auth.oauth.type.ResourceServer.APPLE;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import woohaengsi.qnadiary.RepositoryIntegrationTest;
import woohaengsi.qnadiary.answer.domain.Answer;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.domain.Question;
import woohaengsi.qnadiary.question.repository.QuestionRepository;

@Sql("classpath:/data.sql")
@RepositoryIntegrationTest
class AnswerRepositoryTest {

    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    QuestionRepository questionRepository;

    @DisplayName("주어진 작성자와 답변 ID가 일치하는 답변을 조회한다.")
    @Test
    void findAnswerWithRightIdAndMember() {
        // given
        Question question = questionRepository.save(new Question("1번 질문"));
        Member member = memberRepository.save(
            Member.of(APPLE, "resource_id", "name", "email", "image"));

        Answer answer = new Answer(member, question, "답변 1");
        answerRepository.save(answer);

        // when
        Optional<Answer> findAnswer = answerRepository.findByIdAndMember(answer.getId(), member);

        // then
        assertThat(findAnswer).contains(answer);
    }

    @DisplayName("답변 ID와 작성자가 일치하지 않으면 NULL을 반환한다.")
    @Test
    void findAnswerWithWrongIdAndMember() {
        // given
        Question question = questionRepository.save(new Question("1번 질문"));
        Member member1 = memberRepository.save(
            Member.of(APPLE, "resource_id", "name", "email", "image"));
        Member member2 = memberRepository.save(
            Member.of(APPLE, "resource_id", "name", "email", "image"));
        Answer answer = new Answer(member1, question, "답변 1");
        answerRepository.save(answer);

        // when
        Optional<Answer> findAnswer = answerRepository.findByIdAndMember(answer.getId(), member2);

        // then
        assertThat(findAnswer).isEmpty();
    }

    @DisplayName("해당 기간에 회원이 작성한 모든 글을 조회한다.")
    @Test
    void findAllByMemberAndCreatedAtBetween() {
        // given
        Member member = memberRepository.findById(1L).get();

        YearMonth yearMonth = YearMonth.of(2023, 7);
        LocalDateTime start = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime end = start.plusMonths(1L);

        // when
        List<Answer> answers = answerRepository.findAllByMemberAndCreatedAtBetween(member, start, end);

        // then
        assertThat(answers).hasSize(3);
    }

    @DisplayName("해당 기간에 회원이 해당 질문에 답변한 모든 글을 조회한다.")
    @Test
    void findAllByMemberAndQuestion() {
        // given
        Member member = memberRepository.findById(1L).get();
        Question question = questionRepository.findById(1L).get();

        // when
        List<Answer> answers = answerRepository.findAllByMemberAndQuestion(member, question);

        // then
        assertThat(answers).hasSize(2);
    }
}
