package com.project.room;

import com.project.LogoutHandler;
import com.project.controllers.RoomController;
import com.project.models.Room;
import com.project.repositories.RoomRepository;
import com.project.services.RoomService;
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

@WebMvcTest(RoomController.class)
public class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Room room;

    @MockBean
    private RoomService roomService;

    @MockBean
    private RoomRepository roomRepository;

    @MockBean
    private RoomController roomController;

    @MockBean
    private LogoutHandler logoutHandler;

    @Test
    void itShouldRetrieveRooms() throws Exception {
        Room room = new Room();
        room.setNum(42L);
        room.setFloor("First Floor");
        room.setSize(3L);
        room.setIsAvail(true);
        room.setLink("url");
        room.setAddress("url");
        room.setLat(46.0125);
        room.setLon(25.0567);
        List<Room> roomsList = List.of(room);
        BDDMockito.given(roomService.getRooms()).willReturn(roomsList);
        mockMvc.perform(get("/rooms").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is3xxRedirection());
    }
}
