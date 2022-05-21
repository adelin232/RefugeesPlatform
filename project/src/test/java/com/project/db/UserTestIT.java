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
            User user = new User();
            user.setEmail("me@gmail.com");
            user.setPhone("0758664153");
            user.setFirstName("George");
            user.setLastName("Smith");
            user.setIsAdmin(false);
            userRepository.save(user);
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
