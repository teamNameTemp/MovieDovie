package com.board.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.board.entity.MemberEntity;
import com.board.entity.ReplyEntity;
import com.board.entity.movie.MovieEntity;
import com.board.entity.repository.MemberRepository;
import com.board.entity.repository.ReplyRepository;
import com.board.entity.repository.movie.MovieRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
	private final ReplyRepository replyRepository;
	private final MovieRepository movieRepository;
	private final MemberRepository memberRepository;
	
	@Override
	public List<Map<String,Object>> getList(Long movieId) {
		List<Map<String,Object>> data = new ArrayList<>();
		
		MovieEntity movie = movieRepository.findById(movieId).
				orElseThrow(()->new RuntimeException("movieID not found " + movieId));
		List<ReplyEntity> replies = replyRepository.findAllByMovieId(movie);
		for(ReplyEntity reply : replies) {
			Map<String,Object> map = new HashMap<>();
			map.put("reply", reply.getSeqno());
			map.put("nickname", reply.getEmail().getNickname());
			map.put("content", reply.getReplycontent());
			map.put("regdate", reply.getRegdate());
			
			data.add(map);
		}
		
		return data;
	}

	@Override
	public String saveOrUpdate(String content, String email,Long movieId) {
		String result = "save";
		MemberEntity member = memberRepository.findById(email).get();
		MovieEntity movie = movieRepository.findById(movieId).orElseThrow(()->
			new RuntimeException("movieID not found " + movieId)
		);
		
		ReplyEntity reply = replyRepository.findByEmailAndMovieId(member, movie);
		//저장
		if(reply == null) {
			reply = ReplyEntity.builder()
						.email(member)
						.movieId(movie)
						.replycontent(content)
						.regdate(LocalDateTime.now())
						.build();
		}
		//업데이트
		else {
			reply.setReplycontent(content);
			result = "update";
		}
		
		replyRepository.save(reply);
		return result;
		
	}

	@Override
	public List<Map<String,Object>> getTopList(int topCount) {
		List<Map<String,Object>> data = new ArrayList<>();
		List<Map<String,Object>> replys = replyRepository.findTopMoviesByReplyCount(topCount);
		for(Map<String,Object> d : replys) {
			Long movieId = (Long)d.get("movieId");

			MovieEntity movie = movieRepository.findById(movieId).get();
			Map<String,Object> map = new HashMap<>();
			map.put("info", movie);
			map.put("count", d.get("count"));
			data.add(map);
		}
		
		return data;
	}
}
