package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.dto.UserCreateRequest;
import by.solbegsoft.urlshorteneruaa.exception.NoActivatedAccountException;
import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.service.AuthenticationService;
import by.solbegsoft.urlshorteneruaa.util.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import static by.solbegsoft.urlshorteneruaa.util.UserConstant.BEARER_ADMIN_TOKEN;
import static by.solbegsoft.urlshorteneruaa.util.UserConstant.USER_EMAIL;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectCreator objectCreator;
    @MockBean
    private AuthenticationService authenticationService;

    @Test
    void registration() throws Exception {
        //when
        when(authenticationService.save(any(UserCreateRequest.class))).thenReturn(objectCreator.registerUserResponse());
        //then
        this.mockMvc
                .perform(
                        post("/api/v1/auth/registration")
                                .contentType(APPLICATION_JSON)
                                .content(objectCreator.toJson(objectCreator.userCreateRequest()))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(USER_EMAIL));
    }

    @Test
    void registrationEmailAlreadyExist() throws Exception {
        //when
        when(authenticationService.save(any(UserCreateRequest.class))).thenThrow(UserDataException.class);
        //then
        this.mockMvc
                .perform(
                        post("/api/v1/auth/registration")
                                .contentType(APPLICATION_JSON)
                                .content(objectCreator.toJson(objectCreator.userCreateRequest()))
                )
                .andExpect(status().isConflict());
    }

    @Test
    void registrationWithoutParam() throws Exception {
        this.mockMvc
                .perform(post("/api/v1/auth/registration"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login() throws Exception {
        //when
        when(authenticationService.login(objectCreator.loginUserRequest())).thenReturn(BEARER_ADMIN_TOKEN);
        //then
        this.mockMvc
                .perform(
                        post("/api/v1/auth/login")
                                .contentType(APPLICATION_JSON)
                                .content(objectCreator.toJson(objectCreator.loginUserRequest()))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType("text/plain;charset=UTF-8"))
                .andExpect(content().string(BEARER_ADMIN_TOKEN));
    }

    @Test
    void loginWrongPasswordOrEmail() throws Exception {
        //when
        when(authenticationService.login(objectCreator.loginUserRequest())).thenThrow(UsernameNotFoundException.class);
        //then
        this.mockMvc
                .perform(
                        post("/api/v1/auth/login")
                                .contentType(APPLICATION_JSON)
                                .content(objectCreator.toJson(objectCreator.loginUserRequest()))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void loginWrongBlockedAccount() throws Exception {
        //when
        when(authenticationService.login(objectCreator.loginUserRequest())).thenThrow(NoActivatedAccountException.class);
        //then
        this.mockMvc
                .perform(
                        post("/api/v1/auth/login")
                                .contentType(APPLICATION_JSON)
                                .content(objectCreator.toJson(objectCreator.loginUserRequest()))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void loginWithoutParam() throws Exception {
        this.mockMvc
                .perform(post("/api/v1/auth/login"))
                .andExpect(status().isBadRequest());
    }
}