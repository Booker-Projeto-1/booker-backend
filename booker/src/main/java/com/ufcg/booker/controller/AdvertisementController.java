package com.ufcg.booker.controller;

import com.ufcg.booker.dto.AdvertisementDto;
import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.User;
import com.ufcg.booker.repository.AdRepository;
import com.ufcg.booker.repository.UserRepository;
import com.ufcg.booker.security.LoggedUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
public class AdvertisementController {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    
    public AdvertisementController(AdRepository adRepository, UserRepository userRepository) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/advertisement")
    public ResponseEntity<?> createAd(@RequestBody AdvertisementDto request, @AuthenticationPrincipal LoggedUser loggedUser) {
        if (userRepository.findById(request.idUser()).isEmpty()) {
            return ResponseEntity.badRequest().body(new AdvertisementController.AdvertisementError("Não existe usuário cadastrado com id " + request.idUser()));
        }
        if(!adRepository.findAllByUserIdAndBookId(request.idUser(), request.idBook()).isEmpty()) {
            return ResponseEntity.badRequest().body(new AdvertisementController.AdvertisementError("Já existe anúncio do livro " + request.idBook() +  " cadastrado para o usuário com id " + request.idUser()));
        }

        Advertisement ad = request.toAdvertisement();
        Advertisement savedAd = adRepository.save(ad);
        System.out.println(request.idUser());
        return ResponseEntity.status(CREATED).body(new AdvertisementController.AdResponse(savedAd.getId(), savedAd.getIdUser(), savedAd.getIdBook(), savedAd.getAdDescription(), savedAd.isActive(), savedAd.isBorrowed(), savedAd.getNumberOfBooks()));
    }
    record AdResponse(Long id, Long idUser, Long idBook, String adDescription, boolean isActive, boolean isBorrowed, Integer numberOfBooks) {}
    record AdvertisementError(String error) {}
}
