package com.devdensan.mercadona;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class SecurityLayerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDashboard() throws Exception {
        // given
        String adminToken = generateAdminToken("root", "root");

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/admin")
                .header("Authorization", "Bearer " + adminToken));

        // then
        result.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Welcome to admin dashboard"));
    }

    private String generateAdminToken(String user, String password) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String requestJson = "{\"username\":\""+user+"\",\"password\":\""+password+"\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://localhost:8080/authenticate", requestEntity, String.class);

        if (responseEntity.getStatusCodeValue() == 200) {
            String responseBody = responseEntity.getBody();
            String token = null;
            if (responseBody != null && responseBody.contains("\"token\"")) {
                int startIndex = responseBody.indexOf("\"token\"") + 9;
                int endIndex = responseBody.indexOf("\"", startIndex);
                if (endIndex > startIndex) {
                    token = responseBody.substring(startIndex, endIndex);
                }
            }

            if (token != null) {
                return token;
            }
        }
        return null;
    }

}
