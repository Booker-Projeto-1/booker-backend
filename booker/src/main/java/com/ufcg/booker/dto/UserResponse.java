package com.ufcg.booker.dto;

import com.ufcg.booker.model.User;

public record UserResponse(Long id, String email, String firstName, String lastName, String phoneNumber) {
    public UserResponse(User user) {
        this(user.getId(), user.getEmail(), user.getFirstName(), user.getLastName(), user.getPhoneNumber());
    }
}
