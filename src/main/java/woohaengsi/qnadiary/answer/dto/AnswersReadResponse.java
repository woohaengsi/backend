package woohaengsi.qnadiary.answer.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class AnswersReadResponse {
    private final List<AnswerDetailResponse> detailResponses;

    private AnswersReadResponse(List<AnswerDetailResponse> responses) {
        this.detailResponses = responses;
    }

    public static AnswersReadResponse of(List<AnswerDetailResponse> responses) {
        return new AnswersReadResponse(responses);
    }
}
