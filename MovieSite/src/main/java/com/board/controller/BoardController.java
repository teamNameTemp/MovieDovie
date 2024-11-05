package com.board.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.dto.MovieDTO;
import com.board.dto.ReplyInterface;
import com.board.entity.LikeEntity;
import com.board.service.BoardService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController { 
	
	private final BoardService boardService;
	
	//추천 장르
	@GetMapping("/selectGenre")
	public void getMovieSelect() {}
	
	@GetMapping("/list")
	public void getlist() {}

	//장르 필터
	@PostMapping("/genreCheck")
	public String postGenreCheck() {
		return null;
	}
	
	//영화상세
	@GetMapping("/movieDetail") //?seqno=name
	public String movieDetail(@PathVariable Long seqno, @RequestParam("page") int pageNum,
			@RequestParam(name="keyword",defaultValue="",required=false) String keyword,
			Model model, HttpSession session) throws Exception {

		//세션 email값 가져 오기
		String sessionEmail = (String)session.getAttribute("email");
		
		//좋아요/싫어요 처리
		LikeEntity likeCheckView = service.likeCheckView(seqno,sessionEmail);
		
		//초기에 좋아요/싫어요 등록이 안되어져 있을 경우 "N"으로 초기화
		if(likeCheckView == null) {
			model.addAttribute("LikeCheck", "N");
			model.addAttribute("DislikeCheck", "N");
		} else if(likeCheckView != null) {
			model.addAttribute("LikeCheck", likeCheckView.getLikecheck());
			model.addAttribute("DislikeCheck", likeCheckView.getDislikecheck()); 
		}
		
		model.addAttribute("view", service.view(seqno));
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);	
	}
	

	//좋아요/싫어요 관리
	@ResponseBody
	@PostMapping("/board/likeCheck")
	public String postLikeCheck(@RequestBody Map<String,Object> likeCheckData) throws Exception {
		
		int seq = (int)likeCheckData.get("seqno");
		Long seqno = (long)seq;
		//Long seqno = (Long)likeCheckData.get("seqno");
		String email = (String)likeCheckData.get("email");
		String likecheck = (String)likeCheckData.get("likecheck");
		String dislikecheck = (String)likeCheckData.get("dislikecheck");
		int checkCnt = (int)likeCheckData.get("checkCnt");			

		//tbl_like 테이블 입력/수정
		LikeEntity likeCheckView = service.likeCheckView(seqno,email);
		if(likeCheckView == null) //좋아요/싫어요를 등록한 적이 없음 
				service.likeCheckRegistry(seqno, email, likecheck,
						dislikecheck);
			else //좋아요/싫어요를 등록한 적이 있음
				service.likeCheckUpdate(seqno, email, likecheck,
						dislikecheck); 
		
		long likeCnt = service.view(seqno).getLikecnt();
		long dislikeCnt = service.view(seqno).getDislikecnt();
		
		switch(checkCnt) {
		case 1: likeCnt--; break;
		case 2: likeCnt++; dislikeCnt--; break;
		case 3: likeCnt++; break;
		case 4: dislikeCnt--; break;
		case 5: likeCnt--; dislikeCnt++; break;
		case 6: dislikeCnt++; break;
		}
		
		MovieDTO board = MovieDTO.builder()
							.seqno(seqno)
							.likecnt(likeCnt)
							.dislikecnt(dislikeCnt)
							.build();
		service.boardLikeUpdate(board);
		
		return "{\"likecnt\":\"" + likeCnt + "\",\"dislikecnt\": \"" + dislikeCnt + "\"}";
	}
	
	//댓글 처리
	@ResponseBody
	@PostMapping("/board/reply")
	public List<ReplyInterface> postReply(ReplyInterface reply, @RequestParam("option") String option) throws Exception{
		
		switch(option) {
			
			case "I" : service.replyRegistry(reply); //댓글 등록
						break;
			case "U" : service.replyUpdate(reply); //댓글 수정
						break;
			case "D" : service.replyDelete(reply); //댓글 삭제
					   break;
		}
	
		return service.replyView(reply);
	}
}
