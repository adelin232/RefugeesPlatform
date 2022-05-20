package com.project.room;

import com.project.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class RoomControllerTestIT extends BaseIT {

    @Test
    void itShouldRetrieveRooms() throws Exception {
        //when
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/rooms"));
        //then
        response.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }
}
