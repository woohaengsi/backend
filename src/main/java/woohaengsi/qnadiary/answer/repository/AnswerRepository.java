package woohaengsi.qnadiary.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import woohaengsi.qnadiary.answer.domain.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
