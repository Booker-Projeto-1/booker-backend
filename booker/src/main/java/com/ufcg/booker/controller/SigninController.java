package com.ufcg.booker.controller;

import com.ufcg.booker.dto.SigninDto;
import com.ufcg.booker.repository.UserRepository;
import com.ufcg.booker.model.User;
import com.ufcg.booker.util.CellPhoneValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class SigninController {

    private final UserRepository userRepository;

    public SigninController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> createUser(@RequestBody SigninDto request) {
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.badRequest().body(new SigninError("Email já existente"));
        }
        if (!CellPhoneValidator.validateCellPhone(request.phoneNumber())){
            return ResponseEntity.badRequest().body(new SigninError("Número de celular inválido"));
        }
        User user = request.toUser();
        User savedUser = userRepository.save(user);

        return ResponseEntity.status(CREATED).body(new SigninResponse(savedUser.getId(), savedUser.getEmail()));
    }
    record SigninResponse(Long id, String email) {}

    record SigninError(String error) {}

}
