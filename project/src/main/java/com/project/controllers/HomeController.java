package com.project.controllers;

import com.project.models.Rental;
import com.project.models.Room;
import com.project.models.User;
import com.project.services.RentalService;
import com.project.services.RoomService;
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
 * Controller for the home page.
 */
@Slf4j
@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RentalService rentalService;

    @GetMapping({"/", "/index"})
    @Transactional
    public String home(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    log.info("GET request for / or /index");
                } else {
                    log.info("GET request for / or /index from " + user.getId());
                    addForms(model, user);
                }
            }

            model.addAttribute("profile", principal.getClaims());
        } else {
            log.info("GET request for / or /index");
        }

        return "index";
    }

    public void addForms(Model model, User user) {
        Rental rental = rentalService.findRentalByUserId(user.getId());

        if (rental != null) {
            Long roomId = rental.getRoomId();
            Room room = roomService.getRoom(roomId);

            model.addAttribute("roomForm", room);
            model.addAttribute("rentalForm", rental);
        }

        model.addAttribute("userForm", user);
    }
}
