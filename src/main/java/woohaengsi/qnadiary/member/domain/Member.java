package woohaengsi.qnadiary.member.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import woohaengsi.qnadiary.answer.domain.Answer;
import woohaengsi.qnadiary.auth.oauth.type.ResourceServer;
import woohaengsi.qnadiary.bloomedflower.domain.BloomedFlower;
import woohaengsi.qnadiary.common.BaseEntity;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private final List<Answer> answers = new ArrayList<>();

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private final List<BloomedFlower> bloomedFlowers = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    private ResourceServer resourceServer;
    private String resourceServerId;
    private String nickname;
    private String emailAddress;
    private String profileImageUrl;
    private String refreshToken;
    private Long currentQuestionNumber;
    @Enumerated(value = EnumType.STRING)
    private QuestionSetSize questionSetSize;

    @Builder
    private Member(ResourceServer resourceServer, String resourceServerId, String nickname,
        String emailAddress, String profileImageUrl, String refreshToken) {
        this.resourceServer = resourceServer;
        this.resourceServerId = resourceServerId;
        this.nickname = nickname;
        this.emailAddress = emailAddress;
        this.profileImageUrl = profileImageUrl;
        this.refreshToken = refreshToken;
        this.currentQuestionNumber = 1L;
        this.questionSetSize = QuestionSetSize.MONTH;
    }

    public static Member of(ResourceServer resourceServer, String resourceServerId, String nickname,
        String emailAddress, String profileImageUrl) {
        return Member.builder()
            .resourceServer(resourceServer)
            .resourceServerId(resourceServerId)
            .nickname(nickname)
            .emailAddress(emailAddress)
            .profileImageUrl(profileImageUrl)
            .build();
    }

    public void updateRefreshToken(String newRefreshToken) {
        this.refreshToken = newRefreshToken;
    }

    public void increaseCurrentQuestionNumber() {
        this.currentQuestionNumber++;
    }

    public void repeatQuestionCycle() {
        this.currentQuestionNumber -= this.questionSetSize.getSize();
    }
}
