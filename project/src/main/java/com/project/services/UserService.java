package com.project.services;

import com.project.models.User;
import com.project.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    //@Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    /*public User updateUser(User new_user) {
        User user = getUser(new_user.getEmail());
        user.setFirst_name(new_user.getFirst_name());
        user.setLast_name(new_user.getLast_name());
        user.setEmail(new_user.getEmail());
        user.setPassword(new_user.getPassword());
        user.setPhone(new_user.getPhone());
        user.getAddress().setStreet(new_user.getAddress().getStreet());
        user.getAddress().setNumber(new_user.getAddress().getNumber());
        user.getAddress().setCity(new_user.getAddress().getCity());
        user.getAddress().setCounty(new_user.getAddress().getCounty());
        user.getAddress().setCountry(new_user.getAddress().getCountry());
        return userRepository.save(user);
    }*/

    /*public List<User> deleteUser(Long user_id) {
        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user_id));
        userRepository.delete(user);

        return userRepository.findAll();
    }*/

    /*public User getUser(String email) {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return user;
            }
        }

        return null;
    }*/

    //@Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
