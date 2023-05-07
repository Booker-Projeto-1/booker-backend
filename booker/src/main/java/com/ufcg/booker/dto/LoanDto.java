package com.ufcg.booker.dto;

import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.Loan;
import com.ufcg.booker.model.User;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record LoanDto(

        @NotNull Long borrowerId,

        @NotNull Long advertisementId,

        @NotNull String beginDate,

        @NotNull String endDate
) {
}
