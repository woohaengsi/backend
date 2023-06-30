package woohaengsi.qnadiary.question.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import woohaengsi.qnadiary.question.domain.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

}
