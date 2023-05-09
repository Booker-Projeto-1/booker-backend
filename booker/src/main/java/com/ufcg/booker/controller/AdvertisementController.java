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

import java.util.ArrayList;
import java.util.List;


import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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

        return ResponseEntity.status(CREATED).body(new AdvertisementController.AdvertisementResponse(savedAd.getId(), user.getEmail(), savedAd.getBookId(), savedAd.getDescription(), savedAd.isActive(), savedAd.isBorrowed()));
    }

    @GetMapping("/advertisement/list")
    public ResponseEntity<?> listAds() {

        List<Advertisement> ads = advertisementRepository.findAll();

        List<AdvertisementResponse> adsResponse = ads.stream().filter(ad -> !ad.isBorrowed() && ad.isActive()).map(AdvertisementResponse::new).toList();

        return ResponseEntity.status(OK).body(adsResponse);
    }
    record AdvertisementResponse(Long id, String userEmail, Long bookId, String description, boolean active, boolean borrowed) {
        public AdvertisementResponse(Advertisement ad){
            this(ad.getId(), ad.getUser().getEmail(), ad.getBookId(), ad.getDescription(), ad.isActive(), ad.isBorrowed());
        }
    }


    record AdvertisementError(String error) {}
}
