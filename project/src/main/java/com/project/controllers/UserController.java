package com.project.controllers;

import com.project.models.User;
import com.project.services.UserService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HomeController homeController;

    @PostMapping("/users")
    public void createUser(@Valid @RequestBody User user) {
        log.info("POST request for /users from " + user.getId());
        userService.createUser(user);
    }

    @GetMapping("/users")
    @Transactional
    public String users(Model model, @AuthenticationPrincipal OidcUser principal) {
        log.info("Received GET all users.");

        if (principal != null) {
            int is_new_user = 0;
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    log.info("GET request for /users");
                    is_new_user = 1;
                } else {
                    if (!user.getIsAdmin()) {
                        return "redirect:index";
                    }

                    homeController.addForms(model, user);
                }
            }

            List<User> userList = userService.getUsers();

            model.addAttribute("allUsersForm", userList);
            model.addAttribute("profile", principal.getClaims());
            model.addAttribute("is_new_user", is_new_user);
        }

        return "users";
    }
}
