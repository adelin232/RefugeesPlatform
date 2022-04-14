package com.project.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.models.User;
import com.project.repositories.UserRepository;
import com.project.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Objects;

/**
 * Controller for requests to the {@code /profile} resource. Populates the model with the claims from the
 * {@linkplain OidcUser} for use by the view.
 */
@Controller
public class ProfileController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

//    @GetMapping("/profile")
//    public String profile(Model model, @AuthenticationPrincipal OidcUser oidcUser) {
//        model.addAttribute("profile", oidcUser.getClaims());
//        model.addAttribute("profileJson", claimsToJson(oidcUser.getClaims()));
//
//        return "profile";
//    }

    @GetMapping("/profile")
    @Transactional
    public String profile(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userRepository.findUserByEmail(email);

                model.addAttribute("userForm", Objects.requireNonNullElseGet(user, User::new));
            }

            model.addAttribute("profile", principal.getClaims());
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
                User user = userRepository.findUserByEmail(email);
                userForm.setEmail(email);
                userForm.setFullName(userForm.getFirstName() + " " + userForm.getLastName());

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

    private String claimsToJson(Map<String, Object> claims) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(claims);
        } catch (JsonProcessingException jpe) {
            log.error("Error parsing claims to JSON", jpe);
        }

        return "Error parsing claims to JSON.";
    }
}
