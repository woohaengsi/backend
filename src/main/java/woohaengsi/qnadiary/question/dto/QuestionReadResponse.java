package woohaengsi.qnadiary.question.dto;

import lombok.Builder;
import lombok.Getter;
import woohaengsi.qnadiary.question.domain.Question;

@Getter
public class QuestionReadResponse {

    private final Long id;
    private final String content;

    @Builder
    private QuestionReadResponse(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public static QuestionReadResponse of(Question question) {
        return QuestionReadResponse.builder()
            .id(question.getId())
            .content(question.getContent())
            .build();
    }
}
