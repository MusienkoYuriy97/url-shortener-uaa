package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.dto.UpdatePasswordRequest;
import by.solbegsoft.urlshorteneruaa.exception.ActiveKeyNotValidException;
import by.solbegsoft.urlshorteneruaa.exception.UserDataException;
import by.solbegsoft.urlshorteneruaa.service.UserService;
import by.solbegsoft.urlshorteneruaa.util.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static by.solbegsoft.urlshorteneruaa.util.UserConstant.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectCreator objectCreator;
    @MockBean
    private UserService userService;

    @Test
    void updatePassword() throws Exception {
        //when
        doNothing().when(userService).updatePassword(any(UpdatePasswordRequest.class));
        //then
        this.mockMvc
                .perform(
                        put("/api/v1/user/password")
                                .header("Authorization", BEARER_ADMIN_TOKEN)
                                .contentType(APPLICATION_JSON)
                                .content(objectCreator.toJson(objectCreator.updatePasswordRequest()))
                )
                .andExpect(status().isOk());
    }

    @Test
    void updatePasswordNotMatch() throws Exception {
        //when
        doThrow(UserDataException.class).when(userService).updatePassword(any(UpdatePasswordRequest.class));
        //then
        this.mockMvc
                .perform(
                        put("/api/v1/user/password")
                                .header("Authorization", BEARER_ADMIN_TOKEN)
                                .contentType(APPLICATION_JSON)
                                .content(objectCreator.toJson(objectCreator.updatePasswordRequest()))
                )
                .andExpect(status().isConflict());
    }

    @Test
    void updatePasswordWithoutParam() throws Exception {
        this.mockMvc
                .perform(put("/api/v1/user/password")
                        .header("Authorization", BEARER_ADMIN_TOKEN))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePasswordNotAuthorize() throws Exception {
        this.mockMvc
                .perform(put("/api/v1/user/password"))
                .andExpect(status().isForbidden());
    }

    @Test
    void activate() throws Exception {
        //when
        doNothing().when(userService).activate(any(String.class));
        //then
        this.mockMvc
                .perform(get("/api/v1/user/activate/fgdfnglfdjbnjbnljnbDSFASBVcxzf131vvzcv"))
                .andExpect(status().isAccepted());
    }

    @Test
    void activateKeyNotValid() throws Exception {
        doThrow(ActiveKeyNotValidException.class).when(userService).activate(any(String.class));

        this.mockMvc
                .perform(get("/api/v1/user/activate/fgdfnglfdjbnjbnljnbDSFASBVcxzf131vvzcv"))
                .andExpect(status().isConflict());
    }
}