package com.techx7.techstore.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.techx7.techstore.model.dto.user.RegisterDTO;
import com.techx7.techstore.testUtils.TestData;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthRestControllerTestIT {

    @Value("${mail.port}")
    private int port;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    private GreenMail greenMail;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestData testData;

    @BeforeEach
    void setUp() {
        testData.cleanAllTestData();

        greenMail = new GreenMail(
                new ServerSetup(port, host, "smtp")
        );

        greenMail.start();

        greenMail.setUser(username, password);
    }

    @AfterEach
    void tearDown() {
        greenMail.stop();
    }

    @Test
    void testRegistrationFailure() throws Exception {
        RegisterDTO registerDTO = new RegisterDTO();

        registerDTO.setEmail("invalidMail");
        registerDTO.setUsername("user");
        registerDTO.setPassword("pass");

        ResultActions result = getRegistrationResult(registerDTO);

        result.andExpect(status().isBadRequest());
    }

    @Test
    void testRegistrationSuccess() throws Exception {
        testData.createAndSaveRole();

        RegisterDTO registerDTO = new RegisterDTO();

        registerDTO.setEmail("mike@gmail.com");
        registerDTO.setUsername("mike1234");
        registerDTO.setPassword("mike1234");

        ResultActions result = getRegistrationResult(registerDTO);

        result.andExpect(status().isOk());

        greenMail.waitForIncomingEmail(1);

        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        assertEquals(1, receivedMessages.length);

        MimeMessage registrationMessage = receivedMessages[0];

        assertTrue(registrationMessage.getContent().toString().contains("mike1234"));

        assertEquals("mike@gmail.com", registrationMessage.getAllRecipients()[0].toString());
    }

    private ResultActions getRegistrationResult(RegisterDTO registerDTO) throws Exception {
        String requestBody = objectMapper.writeValueAsString(registerDTO);

        return mockMvc.perform(
                MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .with(csrf().asHeader())
        );
    }

}
