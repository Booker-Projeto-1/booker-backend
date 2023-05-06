package com.ufcg.booker.dto;

import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.Loan;
import com.ufcg.booker.model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public record LoanDto(

        @NotBlank String borrower,

        @NotBlank Long advertisement,

        @NotBlank @DateTimeFormat(pattern = "dd/mm/yyyy") Date beginDate,
        @NotBlank @DateTimeFormat(pattern = "dd/mm/yyyy") Date endDate
) {


    public Loan toLoan(User lender, User borrower, Advertisement ad) {
        return new Loan(lender, borrower, ad, beginDate, endDate);
    }
}
