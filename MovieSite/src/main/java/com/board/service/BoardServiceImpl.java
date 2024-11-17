package com.board.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.board.dto.ReplyInterface;
import com.board.dto.movie.MovieDTO;
import com.board.entity.LikeEntity;
import com.board.entity.movie.MovieEntity;
import com.board.entity.repository.RequestRepository;
import com.board.entity.repository.movie.MovieRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final MovieRepository movieRepository;

	//게시물 목록 보기...배우이름?
	@Override
	public Page<MovieEntity> movieList(int pageNum, int postNum, String keyword) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC,"seqno"));
		return movieRepository.findByTitleContainingOrOriginalTitleContaining(keyword, keyword, pageRequest);
	}
	
	@Override
	public MovieDTO view(Long seqno) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void hitno(Long seqno) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public LikeEntity likeCheckView(Long seqno, String email) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void likeCheckRegistry(Long seqno, String email, String likeCheck, String dislikeCheck) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void likeCheckUpdate(Long seqno, String email, String likeCheck, String dislikeCheck) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ReplyInterface> replyView(ReplyInterface reply) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void replyRegistry(ReplyInterface reply) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replyUpdate(ReplyInterface reply) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void replyDelete(ReplyInterface reply) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void boardLikeUpdate(MovieDTO board) throws Exception {
		// TODO Auto-generated method stub
		
	}


}
