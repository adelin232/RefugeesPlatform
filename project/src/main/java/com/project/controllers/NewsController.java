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

import javax.transaction.Transactional;
import java.util.Map;

/**
 * Controller for the news page.
 */
@Slf4j
@Controller
public class NewsController {

//    @Autowired
//    private NewsService newsService;

    @Autowired
    private UserService userService;

    @Autowired
    private HomeController homeController;

    @GetMapping("/news")
    @Transactional
    public String news(Model model, @AuthenticationPrincipal OidcUser principal) {
        log.debug("Received GET news.");

        if (principal != null) {
            int is_new_user = 0;
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    is_new_user = 1;
                } else {
                    homeController.addForms(model, user);
                }
            }

            model.addAttribute("profile", principal.getClaims());
            // model.addAttribute("profileJson", claimsToJsonString);
            model.addAttribute("is_new_user", is_new_user);
        }

        return "news";
    }

}
