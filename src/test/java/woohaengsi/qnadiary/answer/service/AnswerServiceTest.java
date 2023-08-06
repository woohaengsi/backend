package woohaengsi.qnadiary.answer.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.tuple;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import woohaengsi.qnadiary.ServiceIntegrationTest;
import woohaengsi.qnadiary.answer.domain.Answer;
import woohaengsi.qnadiary.answer.dto.AnswerCreateRequest;
import woohaengsi.qnadiary.answer.dto.AnswerDateResponse;
import woohaengsi.qnadiary.answer.dto.AnswerDetailResponse;
import woohaengsi.qnadiary.answer.repository.AnswerRepository;
import woohaengsi.qnadiary.auth.oauth.type.ResourceServer;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.domain.Question;
import woohaengsi.qnadiary.question.repository.QuestionRepository;

@Sql("classpath:/data.sql")
@ServiceIntegrationTest
class AnswerServiceTest {

    @Autowired
    AnswerService answerService;
    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    QuestionRepository questionRepository;


    @Test
    @DisplayName("답변을 저장한다.")
    void createAnswer() {
        // given
        Member member = Member.of(ResourceServer.KAKAO, "TEST", "TEST", "TEST", "TEST");
        memberRepository.save(member);

        Question question = new Question("질문1");
        questionRepository.save(question);

        AnswerCreateRequest request = new AnswerCreateRequest(question.getId(), "답변1");
        long oldSize = answerRepository.count();

        // when
        Long answerId = answerService.create(request, member.getId());
        List<Answer> responses = answerRepository.findAll();
        long expectedSize = oldSize + 1;

        // then
        assertThat(responses).hasSize((int) expectedSize)
            .extracting("id", "content")
            .contains(
                tuple(answerId, request.getContent())
            );
    }

    @DisplayName("해당하는 ID의 답변을 조회한다.")
    @Test
    void findByIdAndMember() {
        // given
        Member member = Member.of(ResourceServer.KAKAO, "TEST", "TEST", "TEST", "TEST");
        memberRepository.save(member);

        Question question = new Question("질문1");
        questionRepository.save(question);

        AnswerCreateRequest request = new AnswerCreateRequest(question.getId(), "답변1");
        Long answerId = answerService.create(request, member.getId());

        // when
        AnswerDetailResponse response = answerService.findBy(answerId, member.getId());

        // then
        assertThat(response)
            .extracting("id", "question", "answer")
            .contains(answerId, question.getContent(), request.getContent());
    }

    @DisplayName("해당하는 ID의 답변 작성자가 요청한 회원과 다르면 예외가 발생한다.")
    @Test
    void findByIdWithUnmatchedMember() {
        // given
        Member member1 = Member.of(ResourceServer.KAKAO, "TEST", "TEST", "TEST", "TEST");
        Member member2 = Member.of(ResourceServer.KAKAO, "TEST", "TEST", "TEST", "TEST");
        memberRepository.saveAll(List.of(member1, member2));

        Question question = new Question("질문1");
        questionRepository.save(question);

        AnswerCreateRequest request = new AnswerCreateRequest(question.getId(), "답변1");
        Long answerId = answerService.create(request, member1.getId());

        // then
        assertThatThrownBy(() -> answerService.findBy(answerId, member2.getId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 답변입니다.");
    }

    @DisplayName("존재하지 않는 ID의 답변을 요청하면 예외가 발생한다.")
    @Test
    void findByWithNonExistId() {
        // given
        Member member = Member.of(ResourceServer.KAKAO, "TEST", "TEST", "TEST", "TEST");
        memberRepository.save(member);

        Question question = new Question("질문1");
        questionRepository.save(question);

        AnswerCreateRequest request = new AnswerCreateRequest(question.getId(), "답변1");
        answerService.create(request, member.getId());

        // then
        assertThatThrownBy(() -> answerService.findBy(Long.MAX_VALUE, member.getId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 답변입니다.");
    }

    @DisplayName("답변의 내용을 수정한다.")
    @Test
    void update() {
        // given
        Member member = Member.of(ResourceServer.KAKAO, "TEST", "TEST", "TEST", "TEST");
        memberRepository.save(member);

        Question question = new Question("질문1");
        questionRepository.save(question);

        AnswerCreateRequest request = new AnswerCreateRequest(question.getId(), "답변1");
        Long answerId = answerService.create(request, member.getId());

        String updatedContent = "수정된 답변";

        // when
        answerService.update(answerId, member.getId(), updatedContent);
        AnswerDetailResponse response = answerService.findBy(answerId, member.getId());

        // then
        assertThat(response.getAnswer()).isEqualTo(updatedContent);
    }

    @DisplayName("수정을 요청한 유저가 답변의 작성자가 아니면 예외가 발생한다.")
    @Test
    void updateWithNotWriterMember() {
        // given
        Member member1 = Member.of(ResourceServer.KAKAO, "TEST", "TEST", "TEST", "TEST");
        Member member2 = Member.of(ResourceServer.KAKAO, "TEST", "TEST", "TEST", "TEST");
        memberRepository.saveAll(List.of(member1, member2));

        Question question = new Question("질문1");
        questionRepository.save(question);

        AnswerCreateRequest request = new AnswerCreateRequest(question.getId(), "답변1");
        Long answerId = answerService.create(request, member1.getId());

        String updatedContent = "수정된 답변";

        // then
        assertThatThrownBy(() -> answerService.update(answerId, member2.getId(), updatedContent))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 답변입니다.");
    }

    @DisplayName("답변의 내용을 삭제한다.")
    @Test
    void delete() {
        // given
        Member member = Member.of(ResourceServer.KAKAO, "TEST", "TEST", "TEST", "TEST");
        memberRepository.save(member);

        Question question = new Question("질문1");
        questionRepository.save(question);

        AnswerCreateRequest request = new AnswerCreateRequest(question.getId(), "답변1");
        Long answerId = answerService.create(request, member.getId());

        // when
        answerService.delete(answerId, member.getId());
        Optional<Answer> response = answerRepository.findById(answerId);

        // then
        assertThat(response).isEmpty();
    }

    @DisplayName("답변을 요청한 유저가 답변의 작성자가 아니면 예외가 발생한다.")
    @Test
    void deleteWithNotWriterMember() {
        // given
        Member member1 = Member.of(ResourceServer.KAKAO, "TEST", "TEST", "TEST", "TEST");
        Member member2 = Member.of(ResourceServer.KAKAO, "TEST", "TEST", "TEST", "TEST");
        memberRepository.saveAll(List.of(member1, member2));

        Question question = new Question("질문1");
        questionRepository.save(question);

        AnswerCreateRequest request = new AnswerCreateRequest(question.getId(), "답변1");
        Long answerId = answerService.create(request, member1.getId());

        // then
        assertThatThrownBy(() -> answerService.delete(answerId, member2.getId()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("존재하지 않는 답변입니다.");
    }

    @DisplayName("해당 연월에 작성한 답변 작성일을 모두 조회한다.")
    @Test
    void findAnswerDateByYearAndMonth() {
    	// given
        Member member = memberRepository.findById(1L).get();
        int year = 2023;
        int month = 7;

        // when
        List<AnswerDateResponse> responses = answerService.findAnswerDateByYearAndMonth(
            member.getId(), year, month).getDateResponses();

        // then
        assertThat(responses).hasSize(3)
            .extracting("id", "createdAt")
            .containsExactly(
                tuple(4L, LocalDate.of(2023, 7, 31)),
                tuple(3L, LocalDate.of(2023, 7, 11)),
                tuple(2L, LocalDate.of(2023, 7, 1))
            );
    }

    @DisplayName("해당 질문에 유저가 답변한 모든 질문을 조회한다.")
    @Test
    void findAnswersByQuestion() {
    	// given
        Member member = memberRepository.findById(1L).get();
        Question question = questionRepository.findById(1L).get();

        // when
        List<AnswerDetailResponse> responses = answerService.findAnswersByQuestion(member.getId(),
            question.getId()).getResponses();

        // then
        assertThat(responses).hasSize(2)
            .extracting("id", "question", "answer", "createdAt", "lastModifiedAt")
            .containsExactly(
                tuple(6L, question.getContent(), "답변6", LocalDateTime.of(2023, 8, 1, 0, 0), LocalDateTime.of(2023, 8, 1, 0, 0)),
                tuple(1L, question.getContent(), "답변1", LocalDateTime.of(2023, 6, 30, 23, 59), LocalDateTime.of(2023, 6, 30, 23, 59))
            );
    }
    
    @DisplayName("해당 연월에 작성한 답변을 모두 조회한다.")
    @Test
    void findAnswerByYearAndMonth() {
        // given
        Member member = memberRepository.findById(1L).get();
        int year = 2023;
        int month = 7;

        Question question2 = questionRepository.findById(2L).get();
        Question question3 = questionRepository.findById(3L).get();
        Question question4 = questionRepository.findById(4L).get();

        // when
        List<AnswerDetailResponse> responses = answerService.findAnswerByYearAndMonth(
            member.getId(), year, month).getResponses();

        // then
        assertThat(responses).hasSize(3)
            .extracting("id", "question", "answer", "createdAt", "lastModifiedAt")
            .containsExactly(
                tuple(4L, question4.getContent(), "답변4", LocalDateTime.of(2023, 7, 31, 23, 59), LocalDateTime.of(2023, 7, 31, 23, 59)),
                tuple(3L, question3.getContent(), "답변3", LocalDateTime.of(2023, 7, 11, 16, 25), LocalDateTime.of(2023, 7, 11, 16, 25)),
                tuple(2L, question2.getContent(), "답변2", LocalDateTime.of(2023, 7, 1, 0, 0), LocalDateTime.of(2023, 7, 1, 0, 0))
        );
    }
}
