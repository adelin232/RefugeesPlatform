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

    public Room getRoom() {
        List<Room> rooms = roomRepository.findAll();

        return null;
    }

    public List<Room> getRooms() {
        List<Room> rooms = roomRepository.findAll();



        return rooms;
    }
}
