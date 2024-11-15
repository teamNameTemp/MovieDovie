package com.board.service;

import java.util.List;

import com.board.entity.ReplyEntity;

public interface ReplyService {
	public List<ReplyEntity> getList(Long reqseqno);
	
	public void saveOrUpdate(String content, String email, Long movieId);
}
