package com.ufcg.booker.dto;

import com.ufcg.booker.model.User;
import jakarta.validation.constraints.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public record SigninDto(
        @Email @NotBlank String email,
        @NotBlank @Size(min = 8) String password,
        @NotBlank String phoneNumber,
        @NotBlank String firstName,
        String lastName
) {

    private String fullName() {
        return this.firstName + " " + this.lastName;
    }

    public User toUser() {
        return new User(this.email, this.fullName(), this.phoneNumber, new BCryptPasswordEncoder().encode(this.password)); //TODO Refactor it
    }
}