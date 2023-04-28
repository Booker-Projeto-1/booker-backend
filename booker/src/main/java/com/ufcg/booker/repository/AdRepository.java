package com.ufcg.booker.repository;

import com.ufcg.booker.dto.AdvertisementDto;
import com.ufcg.booker.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdRepository extends JpaRepository<Advertisement, Long> {

    @Query("SELECT a FROM Advertisement a WHERE a.idUser = :userId AND a.idBook = :bookId")
    List<Advertisement> findAllByUserIdAndBookId(@Param("userId") Long userId, @Param("bookId") Long bookId);

}
