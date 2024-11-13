package com.board.service;

import java.util.List;

import com.board.dto.movie.MovieDTO;
import com.board.entity.movie.MovieEntity;

public interface MovieAPIService {
	public void saveTheDataBases(List<MovieDTO> list);
	public void saveTheDataBase(MovieDTO movie);
}
