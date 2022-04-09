package com.project.controllers;

import com.project.models.User;
import com.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/users")
    public void createUser(@Valid @RequestBody User user) {
        userService.createUser(user);
    }

    /*@PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }*/

    /*@DeleteMapping("/users/{user_id}")
    public List<User> deleteUser(@PathVariable Long user_id) throws ResourceNotFoundException {
        return userService.deleteUser(user_id);
    }*/

    /*@GetMapping("/users/{user_email}")
    public User getUser(@PathVariable String user_email) {
        return userService.getUser(user_email);
    }*/

    @GetMapping("/users")
    public List<User> getUsers() {
        return userService.getUsers();
    }
}
