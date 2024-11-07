package com.board.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.board.dto.ReplyInterface;
import com.board.entity.LikeEntity;
import com.board.entity.MemberEntity;
import com.board.entity.ReplyEntity;
import com.board.entity.movie.MovieEntity;
import com.board.entity.repository.LikeRepository;
import com.board.entity.repository.MemberRepository;
import com.board.entity.repository.MovieRepository;
import com.board.entity.repository.ReplyRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

	private static final Logger log = LoggerFactory.getLogger(BoardServiceImpl.class);

	private static final String MOVIE_NOT_FOUND = "영화를 찾을 수 없습니다";

	private final MovieRepository movieRepository;
	private final LikeRepository likeRepository;
	private final MemberRepository memberRepository;
	private final ReplyRepository replyRepository;
	
	//게시물 목록 보기
	
	//게시물 조회수 증가
	@Override
	@Transactional
	public void hitno(Long seqno) throws Exception {
		movieRepository.hitno(seqno);
	}
	
	//좋아요/싫어요 등록여부 확인
	@Override
	public LikeEntity likeCheckView(Long movieseqno, String email) throws Exception {
		return Optional.ofNullable(likeRepository.findByMovieseqnoAndEmail(movieseqno, email))
			.orElseThrow(() -> new EntityNotFoundException("좋아요 정보를 찾을 수 없습니다"));
	}
	//좋아요/싫어요 신규 등록
	@Override
	@Transactional
	public void likeCheckRegistry(Long movieseqno, String email, String likeCheck, String dislikeCheck) throws Exception {

	  LikeEntity likeEntity = LikeEntity.builder()
										.movieseqno(movieseqno)
										.email(email)
										.likecheck(likeCheck)
										.dislikecheck(dislikeCheck)
										.build();
		likeRepository.save(likeEntity);		
	}
	//좋아요/싫어요 수정
	@Override  //email > nickname??
	@Transactional
	public void likeCheckUpdate(Long movieseqno, String email, String likeCheck, String dislikeCheck) throws Exception{
		LikeEntity likeEntity = likeRepository.findByMovieseqnoAndEmail(movieseqno, email);
		
		likeEntity.setLikecheck(likeCheck);
		likeEntity.setDislikecheck(dislikeCheck);
		likeRepository.save(likeEntity);
	}
	
	//댓글 목록 보기
	@Override
	public List<ReplyInterface> replyView(ReplyInterface reply) throws Exception{
		return replyRepository.replyView(reply.getSeqno());
	}
	//댓글 등록
	@Override
    @Transactional
	public void replyRegistry(ReplyInterface reply) throws Exception {
		MovieEntity movieEntity = movieRepository.findById(reply.getSeqno())
		.orElseThrow(() -> new EntityNotFoundException("영화를 찾을 수 없습니다"));
		
		MemberEntity memberEntity = memberRepository.findById(reply.getEmail())
		.orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다"));
		
		ReplyEntity replyEntity = ReplyEntity.builder()
											.seqno(movieEntity)
											.email(memberEntity)
											.replywriter(reply.getReplywriter())
											.replycontent(reply.getReplycontent())
											.replyregdate(LocalDateTime.now())
											.build();
		replyRepository.save(replyEntity);
	}
	
	//댓글 수정
    @Override
    @Transactional
    public void replyUpdate(ReplyInterface reply) throws Exception {
        ReplyEntity replyEntity = replyRepository.findById(reply.getReplyseqno())
            .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다"));
            
        replyEntity.setReplycontent(reply.getReplycontent());    
        replyRepository.save(replyEntity);        
    }    	
	
	//댓글 삭제
    @Override
    @Transactional
    public void replyDelete(ReplyInterface reply) throws Exception {
        ReplyEntity replyEntity = replyRepository.findById(reply.getReplyseqno())
            .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다"));
            
        replyRepository.delete(replyEntity);    
    }

	// // 추천 영화 목록 조회 (새로 추가)
	// @Override
    // public List<MovieDTO> getRecommendedMovies(String genre, Long currentMovieId) throws Exception {
    //     return movieRepository.findRecommendedMovies(genre, currentMovieId)
    //         .stream()
    //         .map(MovieDTO::new)
    //         .toList();
    // }

}
