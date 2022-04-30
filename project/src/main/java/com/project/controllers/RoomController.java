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
import java.util.stream.Collectors;

@Controller
public class RoomController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private HomeController homeController;

    @GetMapping("/rooms")
    @Transactional
    public String rooms(Model model, @AuthenticationPrincipal OidcUser principal) {
        if (principal != null) {
            int is_new_user = 0;
            Map<String, Object> claims = principal.getClaims();
            User user;

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                user = userService.findUserByEmail(email);

                if (user == null) {
                    is_new_user = 1;
                } else {
                    homeController.addForms(model, user);
                    addRentalAndRoomsForm(model, user);
                }

                model.addAttribute("userForm", user);
            }

            model.addAttribute("profile", principal.getClaims());
            model.addAttribute("is_new_user", is_new_user);
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
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    return "redirect:index";
                } else {
                    roomForm.setIsAvail(true);
                    roomService.createRoom(roomForm);
                    model.addAttribute("roomForm", roomForm);

                    addRentalAndRoomsForm(model, user);
                }

                model.addAttribute("userForm", user);
            }

            model.addAttribute("profile", principal.getClaims());
        }

        return "rooms";
    }

    private void addRentalAndRoomsForm(Model model, User user) {
        Rental rental = rentalService.findRentalByUserId(user.getId());

        if (rental != null) {
            model.addAttribute("rentalForm", rental);
        }

        List<Room> roomsList = roomService.getRooms();

        if (!user.getIsAdmin()) {
            roomsList = roomsList.stream().filter(Room::getIsAvail)
                    .collect(Collectors.toList());
        }

        model.addAttribute("allRoomsForm", roomsList);
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
                    Rental rental = rentalService.findRentalByUserId(user.getId());

                    if (rental == null) {
                        room.setIsAvail(false);
                        model.addAttribute("rentalForm", new Rental());
                    } else {
                        Long roomId_ = rental.getRoomId();
                        String redirect = "redirect:my_room?roomId=" + roomId_;

                        model.addAttribute("rentalForm", rental);

                        return redirect;
                    }

                    model.addAttribute("roomForm", room);
                }

                model.addAttribute("userForm", user);
            }

            model.addAttribute("profile", principal.getClaims());
            model.addAttribute("is_new_user", is_new_user);
        }

        return "room_view";
    }

    @GetMapping("/my_room")
    @Transactional
    public String handleMyRoomView(Model model, @AuthenticationPrincipal OidcUser principal, @RequestParam(name="roomId") Long roomId) {
        if (principal != null) {
            int is_new_user = 0;
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    is_new_user = 1;
                } else {
                    Rental rental = rentalService.findRentalByUserId(user.getId());

                    if (rental == null) {
                        return "redirect:rooms";
                    } else {
                        long roomId_ = rental.getRoomId();

                        if (!Objects.equals(roomId_, roomId)) {
                            return "redirect:my_room?roomId=" + roomId_;
                        }

                        Room room = roomService.getRoom(roomId_);
                        model.addAttribute("roomForm", room);
                    }
                }

                model.addAttribute("userForm", user);
            }

            model.addAttribute("profile", principal.getClaims());
            model.addAttribute("is_new_user", is_new_user);
        }

        return "my_room";
    }

    @PostMapping("/my_room")
    @Transactional
    public String handleMyRoomView(Model model, @AuthenticationPrincipal OidcUser principal, @RequestParam(name="roomId") Long roomId, @ModelAttribute("rentalForm") Rental rentalForm) {
        if (principal != null) {
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    return "redirect:index";
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

        return "my_room";
    }

    @PostMapping("/room_edit")
    @Transactional
    public String editRoom(Model model, @AuthenticationPrincipal OidcUser principal, @ModelAttribute("roomForm") Room roomForm) {
        if (principal != null) {
            Map<String, Object> claims = principal.getClaims();

            if (claims.containsKey("email")) {
                String email = (String) claims.get("email");
                User user = userService.findUserByEmail(email);

                if (user == null) {
                    return "redirect:index";
                } else {
                    roomService.updateRoom(roomForm);
                }

                model.addAttribute("roomForm", roomForm);
            }

            model.addAttribute("profile", principal.getClaims());
        }

        return "redirect:rooms";
    }
}
