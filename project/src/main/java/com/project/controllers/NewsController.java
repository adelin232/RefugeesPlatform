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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
        news_list = news_list.stream().sorted(Comparator.comparing(News::getPublish_time).reversed())
                .collect(Collectors.toList());

        for (News news : news_list) {
            TimeZone tz = TimeZone.getTimeZone("UTC+3");
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            df.setTimeZone(tz);
            String nowAsISO = df.format(new Date());
            String publishTime = news.getPublish_time().substring(0, 18) + "Z";
            Date d1, d2;

            try {
                d1 = df.parse(nowAsISO);
                d2 = df.parse(publishTime);
                long differenceInTime = d1.getTime() - d2.getTime();

                long differenceInSeconds
                        = TimeUnit.MILLISECONDS
                        .toSeconds(differenceInTime)
                        % 60;

                long differenceInMinutes
                        = TimeUnit
                        .MILLISECONDS
                        .toMinutes(differenceInTime)
                        % 60;

                long differenceInHours
                        = TimeUnit
                        .MILLISECONDS
                        .toHours(differenceInTime)
                        % 24;

                long difference_In_Days
                        = TimeUnit
                        .MILLISECONDS
                        .toDays(differenceInTime)
                        % 365;

                if (difference_In_Days != 0) {
                    news.setPublish_time("Last updated " + difference_In_Days + " days, " +
                            differenceInHours + " hours, " +
                            differenceInMinutes + " minutes, " +
                            differenceInSeconds + " seconds ago");
                } else if (differenceInHours != 0) {
                    news.setPublish_time("Last updated " + differenceInHours + " hours, " +
                            differenceInMinutes + " minutes, " +
                            differenceInSeconds + " seconds ago");
                } else {
                    news.setPublish_time("Last updated " + differenceInMinutes + " minutes, " +
                            differenceInSeconds + " seconds ago");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        model.addAttribute("news_list", news_list);

        return "news";
    }

}
