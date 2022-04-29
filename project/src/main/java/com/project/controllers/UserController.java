package com.project.controllers;

import com.project.models.User;
import com.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
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
    @Transactional
    public String users(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            int is_new_user = 0;
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    is_new_user = 1;
                } else {
                    if (!user.getIsAdmin()) {
                        return "redirect:index";
                    }
                }

                model.addAttribute("userForm", user);
            }

            List<User> userList = userService.getUsers();

            model.addAttribute("allUsersForm", userList);
            model.addAttribute("profile", principal.getClaims());
            model.addAttribute("is_new_user", is_new_user);
        }

        return "users";
    }
}
