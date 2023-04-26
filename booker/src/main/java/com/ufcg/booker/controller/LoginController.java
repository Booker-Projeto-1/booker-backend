package com.ufcg.booker.controller;

import com.ufcg.booker.dto.LoginDto;
import com.ufcg.booker.security.TokenManager;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


@RestController
public class LoginController {

    private final AuthenticationManager authManager;

    private final TokenManager tokenManager;

    public LoginController(AuthenticationManager authManager, TokenManager tokenManager) {
        this.authManager = authManager;
        this.tokenManager = tokenManager;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody @Valid LoginDto request) {
        UsernamePasswordAuthenticationToken authenticationToken = request.build();

        try {
            Authentication authentication = authManager.authenticate(authenticationToken);
            String jwt = tokenManager.generateToken(authentication);
            return ResponseEntity.ok(new LoginResponse(jwt));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    record LoginResponse(String token) {
    }

}
