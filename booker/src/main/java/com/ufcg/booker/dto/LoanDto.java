package com.ufcg.booker.dto;

import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.Loan;
import com.ufcg.booker.model.User;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record LoanDto(

        @NotNull String borrower,

        @NotNull Long advertisement,

        @NotNull Date beginDate,
        @NotNull Date endDate
) {


    public Loan toLoan(User lender, User borrower, Advertisement ad) {
        return new Loan(lender, borrower, ad, beginDate, endDate);
    }
}
