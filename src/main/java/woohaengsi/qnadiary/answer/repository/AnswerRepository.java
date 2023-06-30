package woohaengsi.qnadiary.answer.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import woohaengsi.qnadiary.answer.domain.Answer;
import woohaengsi.qnadiary.member.domain.Member;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    Optional<Answer> findByIdAndMember(Long id, Member member);
}
