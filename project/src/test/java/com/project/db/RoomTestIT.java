package com.project.db;

import com.project.BaseIT;
import com.project.models.Room;
import com.project.repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoomTestIT extends BaseIT {

    @Autowired
    private RoomRepository roomRepository;

    @BeforeEach
    void insertRooms() {
        for (int i = 0; i < 5; ++i) {
            roomRepository.save(new Room());
        }
    }

    @Test
    void itShouldFindAll() {
        //when
        var roomsList = roomRepository.findAll();
        //then
        assertEquals(roomsList.size(), 5);
    }
}
