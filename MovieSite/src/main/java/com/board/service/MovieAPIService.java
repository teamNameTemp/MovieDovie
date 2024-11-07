package com.board.service;

import java.util.List;

import com.board.entity.movie.MovieEntity;

public interface MovieAPIService {
	public void saveTheDataBases(List<MovieEntity> list);
	public void saveTheDataBase(MovieEntity movie);
}
