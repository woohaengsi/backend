package woohaengsi.qnadiary.answer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Getter;
import woohaengsi.qnadiary.answer.domain.Answer;

@Getter
public class AnswerDateResponse {

    private final Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private final LocalDate createdAt;

    private AnswerDateResponse(Long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt.toLocalDate();
    }

    public static AnswerDateResponse of(Answer answer) {
        return new AnswerDateResponse(answer.getId(), answer.getCreatedAt());
    }
}
