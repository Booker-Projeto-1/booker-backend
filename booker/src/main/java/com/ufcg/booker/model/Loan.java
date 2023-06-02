package com.ufcg.booker.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long loanId;

    @ManyToOne
    @JoinColumn(name = "lender_id")
    private User lender;

    @ManyToOne
    @JoinColumn(name = "borrower_id")
    private User borrower;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Advertisement ad;

    private LocalDate beginDate;

    private LocalDate endDate;

    @Deprecated
    public Loan() {
    }

    public Loan(User lender, User borrower, Advertisement ad, LocalDate beginDate, LocalDate endDate) {
        this.lender = lender;
        this.borrower = borrower;
        this.ad = ad;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Advertisement getAd() {
        return ad;
    }

    public void setAd(Advertisement ad) {
        this.ad = ad;
    }


    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public User getLender() {
        return lender;
    }

    public void setLender(User lender) {
        this.lender = lender;
    }

    public Long getId() {
        return this.loanId;
    }

    public void setId(Long id) {
        this.loanId = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Loan loan = (Loan) o;
        return loanId.equals(loan.loanId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanId);
    }
}
