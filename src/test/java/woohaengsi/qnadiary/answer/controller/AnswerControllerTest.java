package woohaengsi.qnadiary.answer.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import woohaengsi.qnadiary.answer.dto.AnswerCreateRequest;
import woohaengsi.qnadiary.answer.dto.AnswerDateByMonthResponse;
import woohaengsi.qnadiary.answer.dto.AnswerDateResponse;
import woohaengsi.qnadiary.answer.dto.AnswerDetailResponse;
import woohaengsi.qnadiary.answer.dto.AnswersReadResponse;
import woohaengsi.qnadiary.answer.service.AnswerService;
import woohaengsi.qnadiary.ControllerIntegrationTestSupport;

class AnswerControllerTest extends ControllerIntegrationTestSupport {

    @MockBean
    private AnswerService answerService;

    @DisplayName("질문에 대한 새로운 답변을 생성한다.")
    @Test
    void create() throws Exception {
    	// given
        AnswerCreateRequest request = new AnswerCreateRequest(1L, "답변1");

        BDDMockito.given(jwtProvider.issueAccessToken(anyLong()))
                .willReturn("accessToken");
        String accessToken = jwtProvider.issueAccessToken(1L);

        // expected
    	mockMvc.perform(
            post("/answer")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, accessToken)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("해당 id를 가진 답변을 상세 조회한다.")
    @Test
    void findById() throws Exception {
    	// given
        String answerId = "1";
        String question = "Mock 질문";
        long memberId = 1L;

        AnswerDetailResponse response = AnswerDetailResponse.builder()
            .id(Long.parseLong(answerId))
            .question(question)
            .build();

        BDDMockito.given(answerService.findBy(anyLong(), anyLong()))
            .willReturn(response);

        BDDMockito.given(jwtProvider.issueAccessToken(anyLong()))
            .willReturn("accessToken");
        String accessToken = jwtProvider.issueAccessToken(memberId);

        // expected
        mockMvc.perform(
                get("/answers/{id}", answerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(answerId))
            .andExpect(jsonPath("$.question").value(question));
    }

    @DisplayName("해당 id를 가진 답변을 수정한다.")
    @Test
    void updateAnswer() throws Exception {
        // given
        String answerId = "1";
        long memberId = 1L;
        String content = "수정된 답변";

        BDDMockito.given(jwtProvider.issueAccessToken(anyLong()))
            .willReturn("accessToken");
        String accessToken = jwtProvider.issueAccessToken(memberId);

        // expected
        mockMvc.perform(
                patch("/answers/{id}", answerId)
                    .content(content)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("해당 id를 가진 답변을 삭제한다.")
    @Test
    void deleteAnswer() throws Exception {
        // given
        String answerId = "1";
        long memberId = 1L;

        BDDMockito.given(jwtProvider.issueAccessToken(anyLong()))
            .willReturn("accessToken");
        String accessToken = jwtProvider.issueAccessToken(memberId);

        // expected
        mockMvc.perform(
                delete("/answers/{id}", answerId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("해당 연,월의 답변 작성일을 조회한다.")
    @Test
    void findDateByYearAndMonth() throws Exception {
        // given
        String answerId = "1";
        long memberId = 1L;
        int year = 2023;
        int month = 7;

        AnswerDateResponse response1 = new AnswerDateResponse(1L,
            LocalDateTime.of(year, month, 1, 0, 0));
        AnswerDateResponse response2 = new AnswerDateResponse(2L,
            LocalDateTime.of(year, month, 11, 16, 25));
        AnswerDateResponse response3 = new AnswerDateResponse(3L,
            LocalDateTime.of(year, month, 31, 23, 59));

        AnswerDateByMonthResponse responses = AnswerDateByMonthResponse.of(
            List.of(response1, response2, response3));

        BDDMockito.given(answerService.findAnswerDateByYearAndMonth(anyLong(), anyInt(), anyInt()))
            .willReturn(responses);

        BDDMockito.given(jwtProvider.issueAccessToken(anyLong()))
            .willReturn("accessToken");
        String accessToken = jwtProvider.issueAccessToken(memberId);

        // expected
        mockMvc.perform(
                get("/answers/days/{year}/{month}", year, month)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.dateResponses.size()").value(responses.getDateResponses().size()))
            .andExpect(jsonPath("$.dateResponses[0].id").value(response1.getId()))
            .andExpect(jsonPath("$.dateResponses[0].createdAt").value(response1.getCreatedAt().toString()))
            .andExpect(jsonPath("$.dateResponses[1].id").value(response2.getId()))
            .andExpect(jsonPath("$.dateResponses[1].createdAt").value(response2.getCreatedAt().toString()))
            .andExpect(jsonPath("$.dateResponses[2].id").value(response3.getId().toString()))
            .andExpect(jsonPath("$.dateResponses[2].createdAt").value(response3.getCreatedAt().toString()));
    }

    @DisplayName("해당 질문에 대한 유저의 답변을 모두 조회한다.")
    @Test
    void findByQuestion() throws Exception {
        // given
        String questionId = "1";
        long memberId = 1L;
        String question = "Mock 질문";
        String answer = "Mock 답변";

        AnswerDetailResponse response1 = AnswerDetailResponse.builder()
            .id(1L)
            .question(question)
            .answer(answer + 1)
            .build();

        AnswerDetailResponse response2 = AnswerDetailResponse.builder()
            .id(2L)
            .question(question)
            .answer(answer + 2)
            .build();

        AnswerDetailResponse response3 = AnswerDetailResponse.builder()
            .id(3L)
            .question(question)
            .answer(answer + 3)
            .build();

        AnswersReadResponse responses = AnswersReadResponse.of(List.of(response1, response2, response3));

        BDDMockito.given(answerService.findAnswersByQuestion(anyLong(), anyLong()))
            .willReturn(responses);

        BDDMockito.given(jwtProvider.issueAccessToken(anyLong()))
            .willReturn("accessToken");
        String accessToken = jwtProvider.issueAccessToken(memberId);

        // expected
        mockMvc.perform(
                get("/answers/question/{questionId}", questionId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.detailResponses.size()").value(responses.getDetailResponses().size()))
            .andExpect(jsonPath("$.detailResponses[0].id").value(response1.getId()))
            .andExpect(jsonPath("$.detailResponses[0].question").value(response1.getQuestion()))
            .andExpect(jsonPath("$.detailResponses[0].answer").value(response1.getAnswer()))
            .andExpect(jsonPath("$.detailResponses[1].id").value(response2.getId()))
            .andExpect(jsonPath("$.detailResponses[1].question").value(response2.getQuestion()))
            .andExpect(jsonPath("$.detailResponses[1].answer").value(response2.getAnswer()))
            .andExpect(jsonPath("$.detailResponses[2].id").value(response3.getId()))
            .andExpect(jsonPath("$.detailResponses[2].question").value(response3.getQuestion()))
            .andExpect(jsonPath("$.detailResponses[2].answer").value(response3.getAnswer()));
    }

    @DisplayName("해당 연월에 유저가 작성한 답변을 조회합니다.")
    @Test
    void findByYearAndMonth() throws Exception {
        // given
        long memberId = 1L;
        String question = "Mock 질문";
        String answer = "Mock 답변";
        int year = 2023;
        int month = 7;

        AnswerDetailResponse response1 = AnswerDetailResponse.builder()
            .id(1L)
            .question(question)
            .answer(answer + 1)
            .build();

        AnswerDetailResponse response2 = AnswerDetailResponse.builder()
            .id(2L)
            .question(question)
            .answer(answer + 2)
            .build();

        AnswerDetailResponse response3 = AnswerDetailResponse.builder()
            .id(3L)
            .question(question)
            .answer(answer + 3)
            .build();

        AnswersReadResponse responses = AnswersReadResponse.of(List.of(response1, response2, response3));

        BDDMockito.given(answerService.findAnswerByYearAndMonth(anyLong(), anyInt(), anyInt()))
            .willReturn(responses);

        BDDMockito.given(jwtProvider.issueAccessToken(anyLong()))
            .willReturn("accessToken");
        String accessToken = jwtProvider.issueAccessToken(memberId);

        // expected
        mockMvc.perform(
                get("/answers/{year}/{month}", year, month)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(HttpHeaders.AUTHORIZATION, accessToken)
            )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.detailResponses.size()").value(responses.getDetailResponses().size()))
            .andExpect(jsonPath("$.detailResponses[0].id").value(response1.getId()))
            .andExpect(jsonPath("$.detailResponses[0].question").value(response1.getQuestion()))
            .andExpect(jsonPath("$.detailResponses[0].answer").value(response1.getAnswer()))
            .andExpect(jsonPath("$.detailResponses[1].id").value(response2.getId()))
            .andExpect(jsonPath("$.detailResponses[1].question").value(response2.getQuestion()))
            .andExpect(jsonPath("$.detailResponses[1].answer").value(response2.getAnswer()))
            .andExpect(jsonPath("$.detailResponses[2].id").value(response3.getId()))
            .andExpect(jsonPath("$.detailResponses[2].question").value(response3.getQuestion()))
            .andExpect(jsonPath("$.detailResponses[2].answer").value(response3.getAnswer()));
    }
}
