package com.ufcg.booker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class CreateUserController {


    @Autowired
    private UserRepository userRepository;

    @PostMapping("/users")
    public Map<String, Object> createUser(@RequestBody CreateUserRequest request) {
        User user = new User(request.email(), request.fullName(), request.phoneNumber(), request.password());
        User savedUser = userRepository.save(user);
        return Map.of("id", savedUser.getId());
    }
}
