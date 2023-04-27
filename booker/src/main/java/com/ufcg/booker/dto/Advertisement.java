package com.ufcg.booker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Advertisement(
        @NotNull Long idUser,
        @NotBlank String idBook,
        boolean active,
        boolean borrowed
) {

    public Advertisement toAdvertisement(){
        return new Advertisement(this.idUser, this.idBook, this.active, this.borrowed);
    }
}
