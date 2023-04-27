package com.ufcg.booker.controller;

import com.ufcg.booker.dto.AdvertisementDto;
import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.User;
import com.ufcg.booker.repository.AdRepository;
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

    public AdvertisementController(AdRepository adRepository) {
        this.adRepository = adRepository;
    }


    @GetMapping("/h")
    public String hello(@AuthenticationPrincipal LoggedUser loggedUser) {
        return String.format("Hello %s!", loggedUser.get().getEmail());
    }

    @PostMapping("/advertisement")
    public ResponseEntity<?> createAd(@RequestBody AdvertisementDto request) {

        //  verificar se ja existe um user
        if (adRepository.existsByIdBookAndIdUser(request.idUser(), request.idBook())) {
            return ResponseEntity.badRequest().body(new AdvertisementController.AdvertisementError("O usuário já possui um anúncio vinculado a esse livro"));
        }
        Advertisement ad = request.toAdvertisement();
        Advertisement savedAd = adRepository.save(ad);

        return ResponseEntity.status(CREATED).body(new AdvertisementController.AdResponse(savedAd.getId(), savedAd.getIdUser(), savedAd.getIdBook()));
    }
    record AdResponse(Long id, Long idUser, String idBook) {}
    record AdvertisementError(String error) {}
}
