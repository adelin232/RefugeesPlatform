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

    public void createRoom(Room room) {
        roomRepository.save(room);
    }

    public void updateRoom(Room new_room) {
        Room room = getRoom(new_room.getId());

        room.setNum(new_room.getNum());
        room.setFloor(new_room.getFloor());
        room.setSize(new_room.getSize());
        room.setLink(new_room.getLink());
        room.setAddress(new_room.getAddress());
        room.setLat(new_room.getLat());
        room.setLon(new_room.getLon());

        roomRepository.save(room);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    public Room getRoom(Long id) {
        List<Room> rooms = getRooms();

        for (Room room : rooms) {
            if (room.getId().equals(id)) {
                return room;
            }
        }

        return null;
    }

    public List<Room> getRooms() {
        return roomRepository.findAll();
    }
}
