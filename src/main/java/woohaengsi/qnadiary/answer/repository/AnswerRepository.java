package woohaengsi.qnadiary.answer.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import woohaengsi.qnadiary.answer.domain.Answer;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.question.domain.Question;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByIdAndMember(Long id, Member member);

    List<Answer> findAllByMemberAndCreatedAtBetween(Member member, LocalDateTime start, LocalDateTime end);
    List<Answer> findAllByMemberAndQuestion(Member member, Question question);
}
