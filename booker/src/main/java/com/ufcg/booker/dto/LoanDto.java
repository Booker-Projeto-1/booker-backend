package com.ufcg.booker.dto;

import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.Loan;
import com.ufcg.booker.model.User;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

public record LoanDto(

        @NotNull Long borrowerId,

        @NotNull Long advertisementId,

        @NotNull @DateTimeFormat(pattern = "dd/mm/yyyy") LocalDate beginDate,
        @NotNull @DateTimeFormat(pattern = "dd/mm/yyyy") @Future LocalDate endDate
) {


    public Loan toLoan(User lender, User borrower, Advertisement ad) {
        return new Loan(lender, borrower, ad, beginDate, endDate);
    }
}
