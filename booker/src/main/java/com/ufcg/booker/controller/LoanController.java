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
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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
    public ResponseEntity<?> createLoan(@RequestBody LoanDto request, @AuthenticationPrincipal LoggedUser loggedUser){
        User user = loggedUser.get();

        if(request.borrowerEmail().equals(user.getEmail())){
            return ResponseEntity.badRequest().body(new LoanError("Não é possível emprestar um livro a si mesmo!"));
        }
        if(!advertisementRepository.existsById(request.advertisementId())){
            return ResponseEntity.badRequest().body(new LoanError("Anuncio inexistente"));
        }
        Advertisement ad = advertisementRepository.findByIdAndUser(request.advertisementId(), user).orElseThrow(() -> new IllegalStateException("Quem tá criando o empréstimo não é o autor do Anúncio"));
        if(ad.isBorrowed()){
            return ResponseEntity.badRequest().body(new LoanError("Livro já emprestado!"));
        }
        ad.setBorrowed(true);
        advertisementRepository.save(ad);

        Optional<User> possibleBorrower = userRepository.findByEmail(request.borrowerEmail());
        if(possibleBorrower.isEmpty()){
            return ResponseEntity.badRequest().body(new LoanError("Usuário inexistente"));
        }
        User borrower = possibleBorrower.get();
        Loan loan = new Loan(user,borrower,ad, request.beginDate(), request.endDate());
        Loan savedLoan = loanRepository.save(loan);

        return ResponseEntity.status(CREATED).body(new LoanResponse(savedLoan.getId(), user.getEmail(), borrower.getEmail(), ad.getBookId(), savedLoan.getBeginDate(), savedLoan.getEndDate()));
    }

    record LoanResponse(Long id, String lender, String borrower, String bookId, LocalDate begin, LocalDate end) {}
    record LoanError(String error) {}
}
