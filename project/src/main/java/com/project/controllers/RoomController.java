package com.project.controllers;

import com.project.models.Room;
import com.project.models.User;
import com.project.repositories.RoomRepository;
import com.project.repositories.UserRepository;
import com.project.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Controller
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/rooms")
    @Transactional
    public String rooms(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userRepository.findUserByEmail(email);

                if (user == null) {
                    return "index";
                } else {
                    model.addAttribute("roomForm", new Room());
                }

                model.addAttribute("userForm", user);
            }

            List<Room> roomsList = roomService.getRooms();

            model.addAttribute("allRoomsForm", roomsList);
            model.addAttribute("profile", principal.getClaims());
        }

        return "rooms";
    }

    @PostMapping("/rooms")
    @Transactional
    public String rooms(Model model, @AuthenticationPrincipal OidcUser principal, @ModelAttribute("roomForm") Room roomForm) {
        if (principal != null) {
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userRepository.findUserByEmail(email);

                if (user == null) {
                    return "index";
                } else {
                    roomService.createRoom(roomForm);
                    model.addAttribute("roomForm", roomForm);
                }

                model.addAttribute("userForm", user);
            }

            List<Room> roomsList = roomService.getRooms();

            model.addAttribute("allRoomsForm", roomsList);
            model.addAttribute("profile", principal.getClaims());
        }

        return "rooms";
    }
}
