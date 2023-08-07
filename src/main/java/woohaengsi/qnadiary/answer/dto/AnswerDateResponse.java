package woohaengsi.qnadiary.answer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import woohaengsi.qnadiary.answer.domain.Answer;

@Schema(description = "답변 작성일 응답")
@Getter
public class AnswerDateResponse {

    @Schema(description = "답변 ID", defaultValue = "1")
    private final Long id;

    @Schema(description = "답변 작성일")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate createdAt;

    public AnswerDateResponse(Long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt.toLocalDate();
    }

    public static AnswerDateResponse of(Answer answer) {
        return new AnswerDateResponse(answer.getId(), answer.getCreatedAt());
    }
}
