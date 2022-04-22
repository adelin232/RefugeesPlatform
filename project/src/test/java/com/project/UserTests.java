package com.project;

import com.project.models.User;
import com.project.repositories.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class UserTests {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("Insert user should work")
    void insertUserTest() {
        User user = new User(1L, "test@test.com", "0747553042", "Narcis-Adelin", "Miulet", false);

        userRepository.save(user);
        assertThat(userRepository.findById(user.getId())).hasValue(user);
    }
}
