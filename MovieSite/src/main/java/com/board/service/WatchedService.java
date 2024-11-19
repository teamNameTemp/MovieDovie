package com.board.service;

import java.util.Map;

public interface WatchedService {
	public void saveOrUpdate(String action, Long movieId, String email);

	public Map<String, Object> getWatchedInfo(String email, Long movieId);

	public Map<String, Object> getAllWatchedInfo(Long movieId);
}
