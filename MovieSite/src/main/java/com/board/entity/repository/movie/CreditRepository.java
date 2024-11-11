package com.board.entity.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.CreditEntity;

public interface CreditRepository extends JpaRepository<CreditEntity, Long> {

}
