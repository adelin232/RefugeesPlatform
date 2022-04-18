package com.project.controllers;

import com.project.models.User;
import com.project.repositories.UserRepository;
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
 * Controller for the news page.
 */
@Controller
public class NewsController {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

//    @Autowired
//    private NewsService newsService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/news")
    @Transactional
    public String news(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            int is_new_user = 0;
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userRepository.findUserByEmail(email);

                model.addAttribute("userForm", user);
            }

            model.addAttribute("profile", principal.getClaims());
            // model.addAttribute("profileJson", claimsToJsonString);
            model.addAttribute("is_new_user", is_new_user);
        }

        return "news";
    }

}
