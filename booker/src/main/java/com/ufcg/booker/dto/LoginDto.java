package com.ufcg.booker.dto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public record LoginDto(
    @NotBlank String email,
    @NotBlank String password) {

    public UsernamePasswordAuthenticationToken build() {
        return new UsernamePasswordAuthenticationToken(this.email, this.password);
    }
}
