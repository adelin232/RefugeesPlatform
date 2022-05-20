package com.project.user;

import com.project.LogoutHandler;
import com.project.controllers.UserController;
import com.project.models.User;
import com.project.repositories.UserRepository;
import com.project.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private User user;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserController userController;

    @MockBean
    private LogoutHandler logoutHandler;

    @Test
    void itShouldRetrieveUsers() throws Exception {
        User user = new User();
        user.setEmail("me@gmail.com");
        user.setPhone("0758664153");
        user.setFirstName("George");
        user.setLastName("Smith");
        user.setIsAdmin(false);
        List<User> usersList = List.of(user);
        BDDMockito.given(userService.getUsers()).willReturn(usersList);
        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is3xxRedirection());
    }
}
