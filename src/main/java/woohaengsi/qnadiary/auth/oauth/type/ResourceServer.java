package woohaengsi.qnadiary.auth.oauth.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResourceServer {
    KAKAO("kakao"),
    APPLE("apple");

    private final String name;
}
