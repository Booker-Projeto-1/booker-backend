package com.ufcg.booker.dto;

import com.ufcg.booker.model.Advertisement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdvertisementDto(
        @NotNull Long idUser,
        @NotNull Long idBook,
        boolean active,
        boolean borrowed
) {

    public Advertisement toAdvertisement(){
        return new Advertisement(this.idUser, this.idBook);
    }
}
