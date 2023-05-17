package com.ufcg.booker.controller;

import com.ufcg.booker.dto.UpdateUserRequest;
import com.ufcg.booker.model.User;
import com.ufcg.booker.repository.UserRepository;
import com.ufcg.booker.security.LoggedUser;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PutMapping("/users")
    public ResponseEntity<Void> updateUser(@RequestBody @Valid UpdateUserRequest updateUserRequest, @AuthenticationPrincipal LoggedUser loggedUser) {
        User user = loggedUser.get();
        user.updateInformation(updateUserRequest);
        userRepository.save(user);
        return ResponseEntity.ok().build();
    }

}

