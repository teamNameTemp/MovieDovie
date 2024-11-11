package com.board.entity.repository.movie;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.CountryEntity;

public interface CountryRepository extends JpaRepository<CountryEntity, Long> {

}
