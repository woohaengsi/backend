package woohaengsi.qnadiary.member.domain;

import lombok.Getter;

@Getter
public enum QuestionSetSize {
    MONTH(30);

    private final int size;
    QuestionSetSize(int size) {
        this.size = size;
    }
}
