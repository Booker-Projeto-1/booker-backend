package com.ufcg.booker.controller;

import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.User;
import com.ufcg.booker.repository.AdvertisementRepository;
import com.ufcg.booker.repository.UserRepository;
import com.ufcg.booker.security.LoggedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final AdvertisementRepository advertisementRepository;

    public UserController(UserRepository userRepository, AdvertisementRepository advertisementRepository) {
        this.userRepository = userRepository;
        this.advertisementRepository = advertisementRepository;
    }

    @GetMapping("/user/me")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal LoggedUser loggedUser) {

        User user = loggedUser.get();
        return ResponseEntity.status(OK).body(new UserController.UserResponse(user));
    }

    @GetMapping("/user/myAdvertisements")
    public ResponseEntity<?> listUserAds(@AuthenticationPrincipal LoggedUser loggedUser) {
        User user = loggedUser.get();
        List<Advertisement> listAds= advertisementRepository.findByUser(user);
        return ResponseEntity.status(OK).body(listAds);
    }

        record UserResponse(Long id, String email, String fullName, String phoneNumber) {
            public UserResponse(User user) {
                this(user.getId(), user.getEmail(), user.getFullName(), user.getPhoneNumber());
            }
        }

}