package woohaengsi.qnadiary.question.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import woohaengsi.qnadiary.question.domain.Question;

@Schema(description = "질문 조회 응답")
@Getter
public class QuestionReadResponse {

    @Schema(description = "질문의 ID", defaultValue = "1")
    private final Long id;
    @Schema(description = "질문의 내용", defaultValue = "지금 하고 싶은 말은?")
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
