package com.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.board.dto.ReplyInterface;
import com.board.dto.movie.KobisMovieDTO;
import com.board.entity.LikeEntity;

@Service
public interface BoardService {
	
	//영화 상세 정보 보기
	public MovieDTO view(Long seqno) throws Exception;
	
	//게시물 조회수 증가
	public void hitno(Long seqno) throws Exception;
	
    // 좋아요/싫어요 관련
    public void boardLikeUpdate(MovieDTO movie) throws Exception;
    public LikeEntity likeCheckView(Long movieseqno, String email) throws Exception;
    public void likeCheckRegistry(Long movieseqno, String email, String likeCheck, String dislikeCheck) throws Exception;
    public void likeCheckUpdate(Long movieseqno, String email, String likeCheck, String dislikeCheck) throws Exception;
	
	//댓글
	public List<ReplyInterface> replyView(ReplyInterface reply) throws Exception;
	public void replyRegistry(ReplyInterface reply) throws Exception;
	public void replyUpdate(ReplyInterface reply) throws Exception;
	public void replyDelete(ReplyInterface reply) throws Exception;	
	
}
