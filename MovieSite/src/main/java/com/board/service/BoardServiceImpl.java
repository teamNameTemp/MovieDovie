package com.board.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.board.dto.MovieDTO;
import com.board.dto.ReplyInterface;
import com.board.entity.LikeEntity;
import com.board.entity.MemberEntity;
import com.board.entity.MovieEntity;
import com.board.entity.ReplyEntity;
import com.board.entity.repository.LikeRepository;
import com.board.entity.repository.MemberRepository;
import com.board.entity.repository.MovieRepository;
import com.board.entity.repository.ReplyRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

	private final MovieRepository movieRepository;
	private final LikeRepository likeRepository;
	private final MemberRepository memberRepository;
	private final ReplyRepository replyRepository;
	
	//게시물 목록 보기
	
	//영화 상세 정보 보기
	@Override
	@Transactional
	public MovieDTO view(Long seqno) throws Exception {
		return movieRepository.findById(seqno).map(view -> new MovieDTO(view)).get();
	}
	
	//게시물 조회수 증가
	@Override
	public void hitno(Long seqno) throws Exception {
		movieRepository.hitno(seqno);
	}
	
	//좋아요/싫어요 등록여부 확인
	@Override
	public LikeEntity likeCheckView(Long movieseqno, String email) throws Exception {
		return likeRepository.findByMovieseqnoAndEmail(movieseqno,email);
	}
	
	//좋아요/싫어요 신규 등록
	@Override
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
	public void likeCheckUpdate(Long movieseqno, String email, String likeCheck, String dislikeCheck) throws Exception{
		LikeEntity likeEntity = likeRepository
				.findByMovieseqnoAndEmail(movieseqno, email);
		
		likeEntity.setLikecheck(likeCheck);
		likeEntity.setDislikecheck(dislikeCheck);
		likeRepository.save(likeEntity);
	}
	
	@Override
	public void boardLikeUpdate(MovieDTO movie) throws Exception{
		MovieEntity movieEntity = movieRepository.findById(movie.getSeqno()).get();
		movieEntity.setLikecnt(movie.getLikecnt());
		movieEntity.setDislikecnt(movie.getDislikecnt());
		movieRepository.save(movieEntity);
	}

	//댓글 목록 보기
	@Override
	public List<ReplyInterface> replyView(ReplyInterface reply) throws Exception{
		return replyRepository.replyView(reply.getSeqno());
	}
	
	//댓글 등록
	@Override
	public void replyRegistry(ReplyInterface reply) throws Exception {
		MovieEntity movieEntity = movieRepository.findById(reply.getSeqno())
		.orElseThrow(() -> new RuntimeException("영화를 찾을 수 없습니다"));
		
MemberEntity memberEntity = memberRepository.findById(reply.getEmail())
		.orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다"));
		
		ReplyEntity replyEntity = ReplyEntity.builder()
											.seqno(movieEntity)
											.movieseqno(movieEntity.getSeqno())
											.email(memberEntity)
											.replywriter(reply.getReplywriter())
											.replycontent(reply.getReplycontent())
											.replyregdate(LocalDateTime.now())
											.build();
		replyRepository.save(replyEntity);
	}
	
	//댓글 수정
	@Override
	public void replyUpdate(ReplyInterface reply) throws Exception{
		ReplyEntity replyEntity = replyRepository
					.findById(reply.getReplyseqno()).get();
		replyEntity.setReplycontent(reply.getReplycontent());	
		replyRepository.save(replyEntity);		
	}	
	
	//댓글 삭제
	@Override
	public void replyDelete(ReplyInterface reply) throws Exception{
		ReplyEntity replyEntity = replyRepository
				.findById(reply.getReplyseqno()).get();
		replyRepository.delete(replyEntity);	
	}

}
