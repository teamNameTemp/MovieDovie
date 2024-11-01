package com.board.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.board.entity.repository.RentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RentServiceImpl implements RentService{

	private final RentRepository rentRepository;
	
	
	//영화 대여 시작
	@Transactional
    @Override
	public void rentMovie(String email, Long movieseqno) {
        rentRepository.rentMovie(email, movieseqno);
        rentRepository.updateStatus(email, movieseqno);
    }
}
