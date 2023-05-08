package com.ufcg.booker.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.Loan;
import com.ufcg.booker.model.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LoanDto(

        @NotNull Long borrowerId,

        @NotNull Long advertisementId,
        @JsonFormat(pattern = "dd/MM/yyyy")
        @NotNull LocalDate beginDate,

        @JsonFormat(pattern = "dd/MM/yyyy")
        @NotNull LocalDate endDate
) {
}
