package woohaengsi.qnadiary.answer;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static woohaengsi.qnadiary.auth.oauth.type.ResourceServer.APPLE;

import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import woohaengsi.qnadiary.DatabaseCleanup;
import woohaengsi.qnadiary.InitRestDocsTest;
import woohaengsi.qnadiary.RestAssuredAndRestDocsTest;
import woohaengsi.qnadiary.answer.repository.AnswerRepository;
import woohaengsi.qnadiary.member.domain.Member;
import woohaengsi.qnadiary.member.repository.MemberRepository;
import woohaengsi.qnadiary.question.domain.Question;
import woohaengsi.qnadiary.question.repository.QuestionRepository;

@DisplayName("API 문서화 : 답변 조회")
@RestAssuredAndRestDocsTest
class AnswerRestDocsTest extends InitRestDocsTest {

    @Autowired
    DatabaseCleanup databaseCleanup;

    @Autowired
    AnswerRepository answerRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    QuestionRepository questionRepository;

    @BeforeEach
    void setUp() {
        memberRepository.save(Member.of(APPLE, "test", "user1", "test@woohaengsi.com", "image.url"));
        questionRepository.save(new Question("첫번째 질문"));
    }

    @AfterEach
    void tearDown() {
        databaseCleanup.afterPropertiesSet();
        databaseCleanup.execute();
    }

    @Test
    @DisplayName("답변을 정상적으로 생성하면 해당 질문 id와 정상 상태 코드를 반환한다.")
    void answer_create() {
        String accessToken  = jwtProvider.issueAccessToken(1L);
        Map<String, String> requestBody = Map.of("questionId", "1", "content", "우리 모두 화이팅입니다아아아아아아아");

    	given(this.spec)
            .contentType(APPLICATION_JSON_VALUE)
            .header(AUTHORIZATION, accessToken)
            .body(requestBody)

        .when()
            .post("/answer")

            .then()
                .statusCode(OK.value())
                .body("questionId", equalTo(1));
    }

}
