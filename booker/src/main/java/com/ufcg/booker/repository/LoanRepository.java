package com.ufcg.booker.repository;

import com.ufcg.booker.model.Loan;
import com.ufcg.booker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LoanRepository  extends JpaRepository<Loan, Long> {


    @Query("SELECT l FROM Loan l WHERE l.borrower = :user")
    List<Loan> findLoansByUser(User user);
}
