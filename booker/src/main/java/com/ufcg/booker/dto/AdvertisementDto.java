package com.ufcg.booker.dto;

import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AdvertisementDto(
        @NotNull Long bookId,
        String description

) {

    public Advertisement toAdvertisement(User user){
        return new Advertisement(user, this.bookId, this.description);
    }
}
