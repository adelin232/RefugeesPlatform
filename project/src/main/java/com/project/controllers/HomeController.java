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

import javax.transaction.Transactional;
import java.util.Map;

/**
 * Controller for the home page.
 */
@Controller
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    @Transactional
    public String home(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            int is_new_user = 0;
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userRepository.findUserByEmail(email);

                if (user == null) {
                    is_new_user = 1;
                    // TODO se continua in profile.html adaugarea de detalii si creare User in DB
                    // TODO redirectionare catre profile.html daca este new_user
                }

                model.addAttribute("userForm", user);
            }

            model.addAttribute("profile", principal.getClaims());
            // model.addAttribute("profileJson", claimsToJsonString);
            model.addAttribute("is_new_user", is_new_user);
        }

        return "index";
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
