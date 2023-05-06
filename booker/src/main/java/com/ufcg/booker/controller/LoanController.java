package com.ufcg.booker.controller;

import com.ufcg.booker.dto.LoanDto;
import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.Loan;
import com.ufcg.booker.model.User;
import com.ufcg.booker.repository.AdvertisementRepository;
import com.ufcg.booker.repository.LoanRepository;
import com.ufcg.booker.repository.UserRepository;
import com.ufcg.booker.security.LoggedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class LoanController {

    private LoanRepository loanRepository;
    private AdvertisementRepository advertisementRepository;

    private UserRepository userRepository;

    public LoanController(LoanRepository loanRepository, AdvertisementRepository advertisementRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.advertisementRepository = advertisementRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/loan")
    public ResponseEntity<?> createLoan(@RequestBody LoanDto request, @AuthenticationPrincipal LoggedUser loggedUser) throws ParseException {
        User user = loggedUser.get();

        if(request.borrowerId().equals(user.getId())){
            return ResponseEntity.badRequest().body(new LoanError("Não é possível emprestar um livro a si mesmo!"));
        }
        if(!advertisementRepository.existsById(request.advertisementId())){
            return ResponseEntity.badRequest().body(new LoanError("Anuncio inexistente"));
        }

        Advertisement ad = advertisementRepository.findById(request.advertisementId()).get();
        if(ad.isBorrowed()){
            return ResponseEntity.badRequest().body(new LoanError("Livro já emprestado!"));
        }

        Optional<User> possibleBorrower = userRepository.findById(request.borrowerId());
        if(possibleBorrower.isEmpty()){
            return ResponseEntity.badRequest().body(new LoanError("Usuário inexistente"));
        }
        User borrower = possibleBorrower.get();

        Loan loan = new Loan(user,borrower,ad, request.beginDate(), request.endDate());
        Loan savedLoan = loanRepository.save(loan);

        return ResponseEntity.status(CREATED).body(new LoanResponse(savedLoan.getId(), user.getEmail(), borrower.getEmail(), ad.getBookId(), savedLoan.getBeginDate(), savedLoan.getEndDate()));
    }

    record LoanResponse(Long id, String lender, String borrower, Long bookId, LocalDate begin, LocalDate end) {}
    record LoanError(String error) {}
}
