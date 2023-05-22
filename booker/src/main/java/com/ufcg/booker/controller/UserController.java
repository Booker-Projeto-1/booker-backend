package com.ufcg.booker.controller;

import com.ufcg.booker.dto.UpdateUserRequest;
import com.ufcg.booker.dto.UserResponse;
import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.User;
import com.ufcg.booker.repository.AdvertisementRepository;
import com.ufcg.booker.repository.UserRepository;
import com.ufcg.booker.security.LoggedUser;
import jakarta.validation.Valid;
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

    @PutMapping("/users")
    public ResponseEntity<UserResponse> updateUser(@RequestBody @Valid UpdateUserRequest updateUserRequest, @AuthenticationPrincipal LoggedUser loggedUser) {
        User user = loggedUser.get();
        user.updateInformation(updateUserRequest);
        User savedUser = userRepository.save(user);
        return ResponseEntity.ok(new UserResponse(savedUser));
    }

    @GetMapping("/user/me")
    public ResponseEntity<?> getUser(@AuthenticationPrincipal LoggedUser loggedUser) {

        User user = loggedUser.get();
        return ResponseEntity.status(OK).body(new UserResponse(user));
    }

    @GetMapping("/user/myAdvertisements")
    public ResponseEntity<?> listUserAds(@AuthenticationPrincipal LoggedUser loggedUser) {
        User user = loggedUser.get();
        List<Advertisement> listAds = advertisementRepository.findByUser(user);
        return ResponseEntity.status(OK).body(listAds);
    }

}
