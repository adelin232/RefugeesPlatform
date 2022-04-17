package com.project.controllers;

import com.project.models.Rental;
import com.project.models.Room;
import com.project.models.User;
import com.project.services.RentalService;
import com.project.services.RoomService;
import com.project.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
public class RoomController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RentalService rentalService;


    @GetMapping("/rooms")
    @Transactional
    public String rooms(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            int is_new_user = 0;
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    is_new_user = 1;
                } else {
                    model.addAttribute("roomForm", new Room());
                }

                model.addAttribute("userForm", user);
            }

            List<Room> roomsList = roomService.getRooms();

            model.addAttribute("allRoomsForm", roomsList);
            model.addAttribute("profile", principal.getClaims());
            model.addAttribute("is_new_user", is_new_user);
        }

        return "rooms";
    }

    @PostMapping("/rooms")
    @Transactional
    public String rooms(Model model, @AuthenticationPrincipal OidcUser principal, @ModelAttribute("roomForm") Room roomForm, @ModelAttribute("rentalForm") Rental rentalForm) {
        if (principal != null) {
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

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

    @GetMapping("/room_view")
    @Transactional
    public String handleViewRoom(Model model, @AuthenticationPrincipal OidcUser principal, @RequestParam(name="roomId") Long roomId) {
        if (principal != null) {
            int is_new_user = 0;
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    is_new_user = 1;
                } else {
                    Room room = roomService.getRoom(roomId);
                    model.addAttribute("roomForm", room);

                    Rental rental = rentalService.findRentalByUserId(user.getId());
                    model.addAttribute("rentalForm", Objects.requireNonNullElseGet(rental, Rental::new));
                }

                model.addAttribute("userForm", user);
            }

            model.addAttribute("profile", principal.getClaims());
            model.addAttribute("is_new_user", is_new_user);
        }

        return "room_view";
    }

    @PostMapping("/room_view")
    @Transactional
    public String handleViewRoom(Model model, @AuthenticationPrincipal OidcUser principal, @RequestParam(name="roomId") Long roomId, @ModelAttribute("rentalForm") Rental rentalForm) {
        if (principal != null) {
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    return "index";
                } else {
                    Room room = roomService.getRoom(roomId);
                    model.addAttribute("roomForm", room);

                    rentalForm.setUserId(user.getId());
                    rentalForm.setRoomId(roomId);
                    rentalService.createRental(rentalForm);
                    model.addAttribute("rentalForm", rentalForm);
                }

                model.addAttribute("userForm", user);
            }

            model.addAttribute("profile", principal.getClaims());
        }

        return "room_view";
    }


}
