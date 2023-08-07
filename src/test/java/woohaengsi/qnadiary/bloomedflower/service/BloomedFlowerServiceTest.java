package woohaengsi.qnadiary.bloomedflower.service;

import static woohaengsi.qnadiary.auth.oauth.type.ResourceServer.APPLE;

import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import woohaengsi.qnadiary.answer.dto.AnswerCreateRequest;
import woohaengsi.qnadiary.answer.service.AnswerService;
import woohaengsi.qnadiary.bloomedflower.domain.BloomedFlower;
import woohaengsi.qnadiary.flower.domain.Flower;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.ServiceIntegrationTest;

@Sql("classpath:/data.sql")
@ServiceIntegrationTest
class BloomedFlowerServiceTest {

    @Autowired
    BloomedFlowerService bloomedFlowerService;
    @Autowired
    AnswerService answerService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("1~30번까지의 질문을 처음 답변하면 1번 꽃인 수선화를 받는다.")
    void get_first_flower() {
    	// given
        Member member = memberRepository.save(
            Member.of(APPLE, "apple", "test_name", "test@test.com", "test_url"));
        for (Long i = 1L; i < 31; i++) {
            answerService.create(new AnswerCreateRequest(i, i + "번 답변"), member.getId());
        }
        // when
        List<BloomedFlower> flowers = member.getBloomedFlowers();
        Flower flower = flowers.get(0).getFlower();
        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(flowers).hasSize(1);
            softAssertions.assertThat(flower.getName()).isEqualTo("수선화");
            softAssertions.assertThat(flower.getFlowerLanguage()).isEqualTo("자기 사랑");
        });
    }
}
