package com.ufcg.booker.controller;

import com.ufcg.booker.dto.CreateUserDto;
import com.ufcg.booker.repository.UserRepository;
import com.ufcg.booker.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CreateUserController {

    private final UserRepository userRepository;

    public CreateUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/users")
    public ResponseEntity<Map<String, Object>> createUser(@RequestBody CreateUserDto request) {
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Usuário já existente"));
        }
        User user = request.toUser();
        User savedUser = userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("id", savedUser.getId()));
    }
}
