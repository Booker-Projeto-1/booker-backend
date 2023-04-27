package com.ufcg.booker.repository;

import com.ufcg.booker.dto.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdRepository extends JpaRepository<Advertisement, Long> {


    boolean existsByBookId(String bookId);

    List<Advertisement> findByBookId(String bookId);
}
