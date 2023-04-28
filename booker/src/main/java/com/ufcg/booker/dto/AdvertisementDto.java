package com.ufcg.booker.dto;

import com.ufcg.booker.model.Advertisement;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdvertisementDto(
        @NotNull Long idUser,
        @NotNull Long idBook,
        String adDescription,
        @NotNull
        Integer numberOfBooks,
        boolean active,
        boolean borrowed
) {

    public Advertisement toAdvertisement(){
        return new Advertisement(this.idUser, this.idBook, this.adDescription, this.numberOfBooks);
    }
}
