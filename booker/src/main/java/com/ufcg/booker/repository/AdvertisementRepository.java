package com.ufcg.booker.repository;

import com.ufcg.booker.model.Advertisement;
import com.ufcg.booker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {

    @Query("SELECT a FROM Advertisement a WHERE a.user = :user AND a.bookId = :bookId")
    List<Advertisement> findAllByUserAndBookId(@Param("user") User user, @Param("bookId") String bookId);

    Optional<Advertisement> findByIdAndUser(Long id, User user);
}
