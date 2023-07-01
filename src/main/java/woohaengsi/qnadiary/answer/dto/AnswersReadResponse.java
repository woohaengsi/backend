package woohaengsi.qnadiary.answer.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class AnswersReadResponse {
    private final List<AnswerDetailResponse> responses;

    private AnswersReadResponse(List<AnswerDetailResponse> responses) {
        this.responses = responses;
    }

    public static AnswersReadResponse of(List<AnswerDetailResponse> responses) {
        return new AnswersReadResponse(responses);
    }
}
