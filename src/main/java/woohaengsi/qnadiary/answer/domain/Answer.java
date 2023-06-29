package woohaengsi.qnadiary.answer.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woohaengsi.qnadiary.common.BaseEntity;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.question.domain.Question;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    private String content;

    public Answer(Member member, Question question, String content) {
        this.member = member;
        this.question = question;
        this.content = content;
    }
}
