package com.techx7.techstore.web;

import com.techx7.techstore.testUtils.TestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.techx7.techstore.constant.Messages.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestData testData;

    @BeforeEach
    void setUp() {
        testData.cleanAllTestData();
    }

    @Test
    void testLoginFailure() throws Exception {
        mockMvc.perform(post("/users/login-error").param("emailOrUsername", "test@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("login"))
                .andExpect(flash().attribute("bad_credentials", "true"))
                .andExpect(flash().attribute("emailOrUsername", "test@example.com"));
    }

    @Test
    void testRegisterSuccess() throws Exception {
        mockMvc.perform(get("/users/register/success"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"))
                .andExpect(flash().attribute("verificationMessage", "Verification link has been sent to your email"));
    }

    @Test
    void testActivateUserAccountWithValidActivationCode() throws Exception {
        String activationCode = "some-code";

        testData.createUserActivationCode(activationCode, false);

        mockMvc.perform(get("/users/activate").param("activation_code", activationCode))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"))
                .andExpect(flash().attribute("userActivated", USER_VERIFIED));
    }

    @Test
    void testHandleActivationCodeNotFoundError() throws Exception {
        String activationCode = "some-code";

        mockMvc.perform(get("/users/activate").param("activation_code", activationCode))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("activationCodeErrorMessage", VERIFICATION_CODE_NOT_VALID));
    }

    @Test
    void testHandleUserAlreadyActivatedError() throws Exception {
        String activationCode = "some-code";

        testData.createUserActivationCode(activationCode, true);

        mockMvc.perform(get("/users/activate").param("activation_code", activationCode))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(flash().attribute("userAlreadyActivated", USER_ALREADY_VERIFIED));
    }

}
