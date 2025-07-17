package lv.mintos.demo.DemoApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class GlobalExceptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void returns400OnValidationError() throws Exception {
        String invalidPayload = """
            {
              "sourceAccount": null,
              "destinationAccount": 2,
              "amount": 10.00,
              "description": "desc"
            }
        """;

        mockMvc.perform(post("/transaction")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Validation")));
    }

    @Test
    void handlesAppLogicException() throws Exception {

        mockMvc.perform(get("/test-app-exception"))
                .andExpect(status().isIAmATeapot())
                .andExpect(content().string("Test exception"));
    }
}
