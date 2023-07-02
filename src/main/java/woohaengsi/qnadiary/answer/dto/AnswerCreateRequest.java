package woohaengsi.qnadiary.answer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "답변 등록 Request")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerCreateRequest {

    @Schema(description = "답변한 질문의 ID", defaultValue = "1")
    private Long questionId;

    @Schema(description = "작성한 답변", defaultValue = "우리 모두 화이팅입니다아아아아아아아")
    private String content;

    public AnswerCreateRequest(Long questionId, String content) {
        this.questionId = questionId;
        this.content = content;
    }
}
