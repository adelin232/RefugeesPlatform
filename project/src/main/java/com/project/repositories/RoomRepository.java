package com.project.repositories;

import com.project.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
//    @Procedure("FIND_USER_BY_EMAIL")
//    User findUserByEmail(String email);
}
