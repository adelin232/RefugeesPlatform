package com.project.user;

import com.project.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class UserControllerTestIT extends BaseIT {

    @Test
    void itShouldRetrieveUsers() throws Exception {
        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/users"));
        //then
        response.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }
}
