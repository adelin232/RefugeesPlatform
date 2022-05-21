package com.project.controllers;

import com.project.models.News;
import com.project.models.User;
import com.project.services.NewsService;
import com.project.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Controller for the news page.
 */
@Slf4j
@Controller
public class NewsController {

    @Autowired
    private UserService userService;

    @Autowired
    private NewsService newsService;

    @Autowired
    private HomeController homeController;

    @GetMapping("/news")
    @Transactional
    public String news(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    log.info("GET request for /news");
                } else {
                    log.info("GET request for /news from " + user.getId());
                    homeController.addForms(model, user);
                }
            }

            model.addAttribute("profile", principal.getClaims());
        } else {
            log.info("GET request for /news");
        }

        List<News> news_list = newsService.getNews("ukraine");
        model.addAttribute("news_list", news_list);

        return "news";
    }

}
