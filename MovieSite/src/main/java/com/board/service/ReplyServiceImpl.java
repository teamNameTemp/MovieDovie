package com.board.service;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.type.descriptor.jdbc.LocalDateJdbcType;
import org.springframework.stereotype.Service;

import com.board.entity.MemberEntity;
import com.board.entity.ReplyEntity;
import com.board.entity.movie.MovieEntity;
import com.board.entity.repository.MemberRepository;
import com.board.entity.repository.ReplyRepository;
import com.board.entity.repository.movie.MovieRepository;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {
	private final ReplyRepository replyRepository;
	private final MovieRepository movieRepository;
	private final MemberRepository memberRepository;
	
	@Override
	public List<ReplyEntity> getList(Long reqseqno) {

		return null;
	}

	@Override
	public void saveOrUpdate(String content, String email,Long movieId) {
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
		}
		
		replyRepository.save(reply);
		
		
	}

}
