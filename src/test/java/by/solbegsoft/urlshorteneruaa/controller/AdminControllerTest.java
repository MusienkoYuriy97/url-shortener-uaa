package by.solbegsoft.urlshorteneruaa.controller;

import by.solbegsoft.urlshorteneruaa.service.AdminService;
import by.solbegsoft.urlshorteneruaa.util.ObjectCreator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static by.solbegsoft.urlshorteneruaa.util.UserConstant.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectCreator objectCreator;
    @MockBean
    private AdminService adminService;

    @Test
    void updateRole() throws Exception {
        //when
        when(adminService.isCurrentAdmin(any(String.class))).thenReturn(false);
        when(adminService.updateUserRole(any())).thenReturn(objectCreator.updateRoleResponse());
        //then
        this.mockMvc
                .perform(
                        patch("/api/v1/admin/role")
                                .header("Authorization", BEARER_ADMIN_TOKEN)
                                .contentType(APPLICATION_JSON)
                                .content(objectCreator.toJson(objectCreator.updateRoleRequest()))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value(USER_EMAIL));
    }

    @Test
    void updateRoleIsCurrentAdmin() throws Exception {
        //when
        when(adminService.isCurrentAdmin(any(String.class))).thenReturn(true);
        //then
        this.mockMvc
                .perform(
                        patch("/api/v1/admin/role")
                                .header("Authorization", BEARER_ADMIN_TOKEN)
                                .contentType(APPLICATION_JSON)
                                .content(objectCreator.toJson(objectCreator.updateRoleRequest()))
                )
                .andExpect(status().isConflict());
    }

    @Test
    void updateRoleNotAuthorize() throws Exception {
        this.mockMvc
                .perform(patch("/api/v1/admin/role"))
                .andExpect(status().isForbidden());
    }

    @Test
    void updateRoleIsUser() throws Exception {
        this.mockMvc
                .perform(patch("/api/v1/admin/role")
                                .header("Authorization", BEARER_USER_TOKEN)
                                .contentType(APPLICATION_JSON)
                                .content(objectCreator.toJson(objectCreator.updateRoleRequest()))
                )
                .andExpect(status().isForbidden());
    }

    @Test
    void updateRoleWithoutParam() throws Exception {
        this.mockMvc
                .perform(patch("/api/v1/admin/role")
                        .header("Authorization", BEARER_ADMIN_TOKEN))
                .andExpect(status().isBadRequest());
    }
}