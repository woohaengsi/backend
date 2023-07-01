package woohaengsi.qnadiary.answer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import woohaengsi.qnadiary.answer.domain.Answer;

@Schema(description = "답변 상세 조회 응답")
@Getter
public class AnswerDetailResponse {
    @Schema(description = "답변 ID", defaultValue = "1")
    private final Long id;
    @Schema(description = "답변한 질문", defaultValue = "지금 하고 싶은 말은?")
    private final String question;
    @Schema(description = "작성한 답변", defaultValue = "우리 모두 화이팅입니다아아아아아아아")
    private final String answer;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime lastModifiedAt;

    @Builder
    private AnswerDetailResponse(Long id, String question, String answer, LocalDateTime createdAt,
        LocalDateTime lastModifiedAt) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
    }

    public static AnswerDetailResponse of(Answer answer) {
        return AnswerDetailResponse.builder()
            .id(answer.getId())
            .question(answer.getQuestion().getContent())
            .answer(answer.getContent())
            .createdAt(answer.getCreatedAt())
            .lastModifiedAt(answer.getLastModifiedAt())
            .build();
    }
}
