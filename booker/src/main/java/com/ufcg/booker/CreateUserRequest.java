package com.ufcg.booker;

import jakarta.validation.constraints.*;

record CreateUserRequest(@Email @NotBlank String email, @NotBlank @Size(min = 8) String password,
                         @NotBlank String phoneNumber, @NotBlank String firstName, String lastName) {

    String fullName() {
        return this.firstName + " " + this.lastName;
    }
}
