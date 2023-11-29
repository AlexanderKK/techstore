package com.techx7.techstore.web;

import com.techx7.techstore.exception.UserAlreadyActivatedException;
import com.techx7.techstore.exception.UserNotActivatedException;
import com.techx7.techstore.service.UserActivationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static com.techx7.techstore.constant.Messages.USER_VERIFIED;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(AuthController.class)
class AuthControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserActivationService userActivationService;

    // Test the login method when the model contains bad credentials.
    @Test
    void testLoginWhenModelContainsBadCredentials() throws Exception {
        mockMvc.perform(get("/users/login").sessionAttr("bad_credentials", true))
                .andExpect(status().isOk())
                .andExpect(view().name("auth-login"))
                .andExpect(model().attribute("bad_credentials", true));
    }

    // Test the login failure method.
    @Test
    void testLoginFailureThenRedirectToLogin() throws Exception {
        mockMvc.perform(post("/users/login-error").param("emailOrUsername", "test@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("login"))
                .andExpect(flash().attribute("bad_credentials", "true"))
                .andExpect(flash().attribute("emailOrUsername", "test@example.com"));
    }

    // Test the register success method.
    @Test
    void testRegisterSuccessThenRedirectToLogin() throws Exception {
        mockMvc.perform(get("/users/register/success"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"))
                .andExpect(flash().attribute("verificationMessage", "Verification link has been sent to your email"));
    }

    // Test the activate user account method with a valid activation code.
    @Test
    void testActivateUserAccountWithValidActivationCodeThenRedirectToLogin() throws Exception {
        String activationCode = "valid-code";
        when(userActivationService.activateUser(activationCode)).thenReturn("User");

        mockMvc.perform(get("/users/activate").param("activation_code", activationCode))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"))
                .andExpect(flash().attribute("userActivated", USER_VERIFIED));
    }

    @Test
    void testHandleUserNotActivatedError() throws Exception {
        when(userActivationService.activateUser("someCode")).thenThrow(UserNotActivatedException.class);

        mockMvc.perform(get("/users/login").requestAttr("exception", new UserNotActivatedException("User not activated")))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/login"));
    }

    @Test
    void testHandleUserAlreadyActivatedError() throws Exception {
        mockMvc.perform(get("/").requestAttr("exception", new UserAlreadyActivatedException("User already activated")))
                .andExpect(status().isBadRequest())
                .andExpect(flash().attribute("userAlreadyActivated", "User already activated"));
    }

}
