package woohaengsi.qnadiary;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import woohaengsi.qnadiary.answer.controller.AnswerController;
import woohaengsi.qnadiary.auth.jwt.JwtProvider;

@ActiveProfiles("test")
@WebMvcTest(controllers = {
    AnswerController.class
})
public abstract class ControllerIntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected JwtProvider jwtProvider;
}
