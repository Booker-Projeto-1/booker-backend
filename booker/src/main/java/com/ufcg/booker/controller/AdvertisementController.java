package com.ufcg.booker.controller;

import com.ufcg.booker.dto.AdvertisementDto;
import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.Loan;
import com.ufcg.booker.model.User;
import com.ufcg.booker.repository.AdvertisementRepository;
import com.ufcg.booker.security.LoggedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class AdvertisementController {

    private final AdvertisementRepository advertisementRepository;

    public AdvertisementController(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    @PostMapping("/advertisement")
    public ResponseEntity<?> createAd(@RequestBody AdvertisementDto request, @AuthenticationPrincipal LoggedUser loggedUser) {
        User user = loggedUser.get();
        if(!advertisementRepository.findAllByUserAndBookId(user, request.bookId()).isEmpty()) {
            return ResponseEntity.badRequest().body(new AdvertisementError("Já existe anúncio do livro " + request.bookId() +  " cadastrado para o usuário com id " + user.getId()));
        }

        Advertisement ad = request.toAdvertisement(user);
        Advertisement savedAd = advertisementRepository.save(ad);

        return ResponseEntity.status(CREATED).body(new AdvertisementResponse(savedAd));
    }

    @GetMapping("/advertisement/list")
    public ResponseEntity<?> listAds(@RequestParam Optional<List<String>> bookIds) {

        List<Advertisement> ads = bookIds.map(advertisementRepository::findAllByBookIdIn)
                                         .orElseGet(advertisementRepository::findAll);

        List<AdvertisementResponse> adsResponse = ads.stream()
                                                     .filter(ad -> !ad.isBorrowed() && ad.isActive())
                                                     .map(AdvertisementResponse::new)
                                                     .toList();

        return ResponseEntity.status(OK).body(adsResponse);
    }

    @PutMapping("/advertisement/update")
    public ResponseEntity<?> updateAdvertisement(@RequestBody AdvertisementUpdate advertisementUpdate, @AuthenticationPrincipal LoggedUser loggedUser){
        User user = loggedUser.get();
        Optional<Advertisement> optionalAdvertisement = advertisementRepository.findByIdAndUser(advertisementUpdate.id, user);
        if( optionalAdvertisement.isEmpty()){
            return ResponseEntity.badRequest().body(new AdvertisementError("Não existe anúncio com id " + advertisementUpdate.id + " para o usuário de id " + user.getId()));
        }
        Advertisement advertisement = optionalAdvertisement.get();
        advertisement.updateAdvertisement(advertisementUpdate.description, advertisementUpdate.active, advertisementUpdate.borrowed);
        Advertisement updatedAd = advertisementRepository.save(advertisement);

        return ResponseEntity.status(OK).body(new AdvertisementResponse(updatedAd));
    }

    @DeleteMapping("/advertisement/delete/{id}")
    public ResponseEntity<?> deleteAdvertisement(@PathVariable long id, @AuthenticationPrincipal LoggedUser loggedUser) {
        User user = loggedUser.get();
        Optional<Advertisement> optionalAdvertisement = advertisementRepository.findByIdAndUser(id, user);
        if( optionalAdvertisement.isEmpty()){
            return ResponseEntity.badRequest().body(new AdvertisementError("Não existe anúncio com id " + id + " para o usuário de id " + user.getId()));
        }
        advertisementRepository.delete(optionalAdvertisement.get());
        return ResponseEntity.ok().build();
    }

    record AdvertisementUpdate(Long id, String description, boolean active, boolean borrowed) {}

    record AdvertisementResponse(Long id, String userEmail, String phoneNumber, String bookId, String description, boolean active, boolean borrowed, List<LoanSummary> loans) {
        public AdvertisementResponse(Advertisement ad){
            this(ad.getId(), ad.getUser().getEmail(), ad.getUser().getPhoneNumber(), ad.getBookId(), ad.getDescription(), ad.isActive(), ad.isBorrowed(), convertLoans(ad.getLoans()));
        }

        private static List<LoanSummary> convertLoans(List<Loan> loans) {
            return loans.stream()
                    .map(LoanSummary::new)
                    .toList();
        }
        record LoanSummary(String borrowerEmail, LocalDate beginDate, LocalDate endDate) {
            public LoanSummary(Loan loan) {
                this(loan.getBorrower().getEmail(), loan.getBeginDate(), loan.getEndDate());
            }
        }
    }
    record AdvertisementError(String error) {}
}
