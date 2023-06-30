package woohaengsi.qnadiary.answer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import woohaengsi.qnadiary.answer.domain.Answer;

@Getter
public class AnswerDetailResponse {
    private final Long id;
    private final String question;
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
