package com.board.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.board.dto.RequestReplyDTO;
import com.board.entity.MemberEntity;
import com.board.entity.RequestEntity;
import com.board.entity.RequestReplyEntity;
import com.board.entity.repository.MemberRepository;
import com.board.entity.repository.RequestReplyRepository;
import com.board.entity.repository.RequestRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RequestReplyServiceImpl implements RequestReplyService{

	private final RequestReplyRepository requestReplyRepository;
	private final MemberRepository memberRepository;
	private final RequestRepository requestRepository;
	
	
	//시작할 때 댓글 목록 가져오기
	@Override	
	public List<RequestReplyEntity> replyList(Long reqseqno){
		return requestReplyRepository.replyList(reqseqno);
	}
	
	
	//댓글 등록
	public void replyRegister(RequestReplyDTO requestReplyDTO) {
		
		RequestEntity requestEntity = requestRepository.findById(requestReplyDTO.getSeqno())
															.orElseThrow(() -> new EntityNotFoundException("Request not found"));
		MemberEntity memberEntity = memberRepository.findById(requestReplyDTO.getEmail())
															.orElseThrow(() -> new EntityNotFoundException("Request not found"));
		
		RequestReplyEntity requestReplyEntity = RequestReplyEntity.builder()
																.reqseqno(requestEntity.getSeqno())
																.email(memberEntity.getEmail())
																.replywriter(requestReplyDTO.getReplywriter())
																.replyregdate(LocalDateTime.now())
																.replycontent(requestReplyDTO.getReplycontent())
																.build();
		requestReplyRepository.save(requestReplyEntity);
	}
	
	
	//댓글 수정
	public void replyUpdate(RequestReplyDTO requestReplyDTO) {
		RequestReplyEntity requestReplyEntity = requestReplyRepository.findById(requestReplyDTO.getSeqno())
																		.orElseThrow(() -> new EntityNotFoundException("Request not found"));
		requestReplyEntity.setReplycontent(requestReplyDTO.getReplycontent());
		requestReplyRepository.save(requestReplyEntity);
	}
	
	
	
	//댓글 삭제
	public void replyDelete(Long seqno) {
		requestReplyRepository.deleteById(seqno);
	}
	
}
