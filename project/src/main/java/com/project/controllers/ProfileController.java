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

    @GetMapping("/profile")
    @Transactional
    public String profile(Model model, @AuthenticationPrincipal OidcUser principal) {
        log.info("Received GET profile.");

        if (principal != null) {
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    model.addAttribute("userForm", new User());
                } else {
                    homeController.addForms(model, user);
                }
            }

            model.addAttribute("profile", principal.getClaims());
        }

        return "profile";
    }

    @PostMapping("/profile")
    @Transactional
    public String profile(Model model, @AuthenticationPrincipal OidcUser principal, @ModelAttribute("userForm") User userForm) {
        log.info("Received POST profile.");

        if (principal != null) {
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);
                userForm.setEmail(email);
                userForm.setIsAdmin(false);

                if (user == null) {
                    userService.createUser(userForm);
                } else {
                    userService.updateUser(userForm);
                }

                model.addAttribute("userForm", userForm);
            }

            model.addAttribute("profile", principal.getClaims());
        }

        return "profile";
    }
}
