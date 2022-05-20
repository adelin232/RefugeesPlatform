package com.project.db;

import com.project.BaseIT;
import com.project.models.User;
import com.project.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserTestIT extends BaseIT {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void insertUsers() {
        for (int i = 0; i < 5; ++i) {
            userRepository.save(new User());
        }
    }

    @Test
    void itShouldFindAll() {
        //when
        var usersList = userRepository.findAll();
        //then
        assertEquals(usersList.size(), 5);
    }
}
