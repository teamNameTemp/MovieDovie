package com.board.service;

import java.util.Map;

public interface BookmarkService {
	public void saveOrUpdate(String action, Long movieId, String email);

	public Map<String, Object> getBookmarkInfo(String email, Long movieId);

	public Map<String, Object> getAllBookmarkInfo(Long movieId);
}
