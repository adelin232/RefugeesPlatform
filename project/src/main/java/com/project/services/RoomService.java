package com.project.services;

import com.project.models.Room;
import com.project.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    public Room getRoom(Long id) {
        return roomRepository.getById(id);
    }

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }
}
