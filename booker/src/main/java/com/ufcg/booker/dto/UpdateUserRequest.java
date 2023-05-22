package com.ufcg.booker.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(@NotBlank String phoneNumber,
                                @NotBlank String firstName,
                                String lastName) {}
