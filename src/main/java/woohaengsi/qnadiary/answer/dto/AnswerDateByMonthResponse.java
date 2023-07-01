package woohaengsi.qnadiary.answer.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class AnswerDateByMonthResponse {
    List<AnswerDateResponse> dateResponses;

    private AnswerDateByMonthResponse(List<AnswerDateResponse> dateResponses) {
        this.dateResponses = dateResponses;
    }

    public static AnswerDateByMonthResponse of(List<AnswerDateResponse> dateResponses) {
        return new AnswerDateByMonthResponse(dateResponses);
    }
}
