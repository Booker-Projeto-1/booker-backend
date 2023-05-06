package com.ufcg.booker.controller;

import com.ufcg.booker.dto.AdvertisementDto;
import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.User;
import com.ufcg.booker.repository.AdvertisementRepository;
import com.ufcg.booker.repository.UserRepository;
import com.ufcg.booker.security.LoggedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class AdvertisementController {

    private final AdvertisementRepository advertisementRepository;
    private final UserRepository userRepository;
    
    public AdvertisementController(AdvertisementRepository advertisementRepository, UserRepository userRepository) {
        this.advertisementRepository = advertisementRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/advertisementId")
    public ResponseEntity<?> createAd(@RequestBody AdvertisementDto request, @AuthenticationPrincipal LoggedUser loggedUser) {
        User user = loggedUser.get();
        if(!advertisementRepository.findAllByUserAndBookId(user, request.bookId()).isEmpty()) {
            return ResponseEntity.badRequest().body(new AdvertisementController.AdvertisementError("Já existe anúncio do livro " + request.bookId() +  " cadastrado para o usuário com id " + user.getId()));
        }

        Advertisement ad = request.toAdvertisement(user);
        Advertisement savedAd = advertisementRepository.save(ad);

        return ResponseEntity.status(CREATED).body(new AdvertisementController.AdvertisementResponse(savedAd.getId(), user, savedAd.getBookId(), savedAd.getDescription(), savedAd.isActive(), savedAd.isBorrowed()));
    }
    record AdvertisementResponse(Long id, User user, Long bookId, String description, boolean active, boolean borrowed) {}
    record AdvertisementError(String error) {}
}
