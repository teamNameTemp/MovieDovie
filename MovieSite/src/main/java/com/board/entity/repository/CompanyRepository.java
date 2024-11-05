package com.board.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.board.entity.movie.CompanyEntity;

public interface CompanyRepository extends JpaRepository<CompanyEntity,Long>{

}
