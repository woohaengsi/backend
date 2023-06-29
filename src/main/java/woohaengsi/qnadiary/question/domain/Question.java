package woohaengsi.qnadiary.question.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woohaengsi.qnadiary.answer.domain.Answer;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "question")
    private final List<Answer> answers = new ArrayList<>();

    private String content;

    public Question(String content) {
        this.content = content;
    }
}
