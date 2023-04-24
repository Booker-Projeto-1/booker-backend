package com.ufcg.booker.dto;

import jakarta.validation.constraints.*;

public record CreateUserDto(@Email @NotBlank String email, @NotBlank @Size(min = 8) String password,
                            @NotBlank String phoneNumber, @NotBlank String firstName, String lastName) {

    public String fullName() {
        return this.firstName + " " + this.lastName;
    }
}
