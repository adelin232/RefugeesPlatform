package com.project.services;

import com.project.models.User;
import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(User new_user) {
        User user = getUser(new_user.getEmail());
        user.setFirst_name(new_user.getFirst_name());
        user.setLast_name(new_user.getLast_name());
        user.setFull_name(new_user.getFull_name());
        user.setEmail(new_user.getEmail());
        user.setPhone(new_user.getPhone());
        userRepository.save(user);
    }

    public List<User> deleteUser(String email) {
        User user = userRepository.findUserByEmail(email);

        if (user == null) {
            return null;
        }

        userRepository.delete(user);

        return userRepository.findAll();
    }

    public User getUser(String email) {
        List<User> users = getUsers();

        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }

        return null;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
