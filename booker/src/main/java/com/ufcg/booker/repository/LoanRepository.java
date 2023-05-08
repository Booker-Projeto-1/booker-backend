package com.ufcg.booker.repository;

import com.ufcg.booker.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoanRepository  extends JpaRepository<Loan, Long> {
}
