package com.ufcg.booker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LoanDto(

        @NotNull String borrowerEmail,

        @NotNull Long advertisementId,
        @JsonFormat(pattern = "dd/MM/yyyy")
        @NotNull LocalDate beginDate,

        @JsonFormat(pattern = "dd/MM/yyyy")
        @NotNull LocalDate endDate
) {
}
