package com.project.controllers;

import com.project.Constants;
import com.project.models.User;
import com.project.services.UserService;
import com.project.statuses.UserStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;
import java.util.Map;

@Slf4j
@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private HomeController homeController;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/profile")
    @Transactional
    public String profile(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    log.info("GET request for /profile");
                    model.addAttribute("userForm", new User());
                } else {
                    log.info("GET request for /profile from " + user.getId());
                    homeController.addForms(model, user);
                }
            }

            model.addAttribute("profile", principal.getClaims());
        } else {
            log.info("GET request for /profile");
        }

        return "profile";
    }

    @PostMapping("/profile")
    @Transactional
    public String profile(Model model, @AuthenticationPrincipal OidcUser principal, @ModelAttribute("userForm") User userForm) {
        if (principal != null) {
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);
                userForm.setEmail(email);
                userForm.setIsAdmin(false);

                if (user == null) {
                    log.info("POST request for /profile");
                    userService.createUser(userForm);
                    UserStatus userStatus = new UserStatus(userForm, "PROCESS", "User Successfully Created" +
                                    userForm.getFirstName() + " " + userForm.getLastName());
                    rabbitTemplate.convertAndSend(Constants.EXCHANGE, Constants.ROUTING_KEY, userStatus);
                } else {
                    log.info("POST request for /profile from " + user.getId());
                    userService.updateUser(userForm);
                }

                model.addAttribute("userForm", userForm);
            }

            model.addAttribute("profile", principal.getClaims());
        } else {
            log.info("POST request for /profile");
        }

        return "profile";
    }
}
