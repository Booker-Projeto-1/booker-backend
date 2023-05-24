package com.ufcg.booker.controller;

import com.ufcg.booker.dto.AdvertisementDto;
import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.User;
import com.ufcg.booker.repository.AdvertisementRepository;
import com.ufcg.booker.repository.UserRepository;
import com.ufcg.booker.security.LoggedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
public class AdvertisementController {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    
    public AdvertisementController(AdvertisementRepository advertisementRepository, UserRepository userRepository) {
        this.advertisementRepository = advertisementRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/advertisement")
    public ResponseEntity<?> createAd(@RequestBody AdvertisementDto request, @AuthenticationPrincipal LoggedUser loggedUser) {
        User user = loggedUser.get();
        if(!advertisementRepository.findAllByUserAndBookId(user, request.bookId()).isEmpty()) {
            return ResponseEntity.badRequest().body(new AdvertisementController.AdvertisementError("Já existe anúncio do livro " + request.bookId() +  " cadastrado para o usuário com id " + user.getId()));
        }

        Advertisement ad = request.toAdvertisement(user);
        Advertisement savedAd = advertisementRepository.save(ad);

        return ResponseEntity.status(CREATED).body(new AdvertisementController.AdvertisementResponse(savedAd.getId(), user.getEmail(), user.getPhoneNumber(), savedAd.getBookId(), savedAd.getDescription(), savedAd.isActive(), savedAd.isBorrowed()));
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
            return ResponseEntity.badRequest().body(new AdvertisementController.AdvertisementError("Não existe anúncio com id " + advertisementUpdate.id + " para o usuário de id " + user.getId()));
        }
        Advertisement advertisement = optionalAdvertisement.get();
        advertisement.updateAdvertisement(advertisementUpdate.description, advertisementUpdate.active, advertisementUpdate.borrowed);
        Advertisement updatedAd = advertisementRepository.save(advertisement);

        return ResponseEntity.status(OK).body(new AdvertisementController.AdvertisementResponse(updatedAd.getId(), updatedAd.getUser().getEmail(), updatedAd.getUser().getPhoneNumber(), updatedAd.getBookId(), updatedAd.getDescription(), updatedAd.isActive(), updatedAd.isBorrowed()));
    }

    @DeleteMapping("/advertisement/delete/{id}")
    public ResponseEntity<?> deleteAdvertisement(@PathVariable long id, @AuthenticationPrincipal LoggedUser loggedUser) {
        User user = loggedUser.get();
        Optional<Advertisement> optionalAdvertisement = advertisementRepository.findByIdAndUser(id, user);
        if( optionalAdvertisement.isEmpty()){
            return ResponseEntity.badRequest().body(new AdvertisementController.AdvertisementError("Não existe anúncio com id " + id + " para o usuário de id " + user.getId()));
        }
        advertisementRepository.delete(optionalAdvertisement.get());
        return ResponseEntity.ok().build();
    }

    record AdvertisementUpdate(Long id, String description, boolean active, boolean borrowed) {
        public AdvertisementUpdate(AdvertisementUpdate advertisementUpdate) {
            this(advertisementUpdate.id(), advertisementUpdate.description(), advertisementUpdate.active(), advertisementUpdate.borrowed());
        }
    }

    record AdvertisementResponse(Long id, String userEmail, String phoneNumber, String bookId, String description, boolean active, boolean borrowed) {
        public AdvertisementResponse(Advertisement ad){
            this(ad.getId(), ad.getUser().getEmail(), ad.getUser().getPhoneNumber(), ad.getBookId(), ad.getDescription(), ad.isActive(), ad.isBorrowed());
        }
    }
    record AdvertisementError(String error) {}
}
