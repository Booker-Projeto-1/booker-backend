package com.ufcg.booker.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(@Email @NotBlank String email,
                                @NotBlank String phoneNumber,
                                @NotBlank String firstName,
                                String lastName) {

    public String fullName() {
        return firstName + " " + lastName;
    }
}
