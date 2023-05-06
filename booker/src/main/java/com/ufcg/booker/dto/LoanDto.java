package com.ufcg.booker.dto;

import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.Loan;
import com.ufcg.booker.model.User;
import jakarta.validation.constraints.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public record LoanDto(

        @NotNull String borrower,

        @NotNull Long advertisement,

        @NotNull String beginDate,
        @NotNull String endDate
) {


    public Loan toLoan(User lender, User borrower, Advertisement ad) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date begin = simpleDateFormat.parse(beginDate);
        Date end = simpleDateFormat.parse(endDate);
        System.out.println(begin);
        System.out.println(end);

        return new Loan(lender, borrower, ad, begin, end);
    }
}
