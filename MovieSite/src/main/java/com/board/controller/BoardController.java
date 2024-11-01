package com.board.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

@Controller
@RequestMapping("/board")
public class BoardController { 
	
	private final BoardService service;
	
	@Autowired
	public BoardController(BoardService service) {
		this.service = service;
	}
	
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
	@GetMapping("/movieDetail")
	public void getView(@RequestParam("seqno") Long seqno, @RequestParam("page") int pageNum,
			@RequestParam(name="keyword",defaultValue="",required=false) String keyword,
			Model model, HttpSession session) throws Exception {

		//세션 email값 가져 오기
		String sessionEmail = (String)session.getAttribute("email");
		
		//좋아요/싫어요 처리
		LikeEntity likeCheckView = service.likeCheckView(seqno,sessionEmail);
		
		//초기에 좋아요/싫어요 등록이 안되어져 있을 경우 "N"으로 초기화
		if(likeCheckView == null) {
			model.addAttribute("myLikeCheck", "N");
			model.addAttribute("myDislikeCheck", "N");
		} else if(likeCheckView != null) {
			model.addAttribute("myLikeCheck", likeCheckView.getMylikecheck());
			model.addAttribute("myDislikeCheck", likeCheckView.getMydislikecheck()); 
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
		String mylikecheck = (String)likeCheckData.get("mylikecheck");
		String mydislikecheck = (String)likeCheckData.get("mydislikecheck");
		int checkCnt = (int)likeCheckData.get("checkCnt");		
		
		//현재 날짜, 시간 구해서 좋아요/싫어요 한 날짜/시간 입력 및 수정
		String likeDate = "";
		String dislikeDate = "";
		LocalDateTime now = LocalDateTime.now();
		if(likeCheckData.get("mylikecheck").equals("Y")) {
			likeDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));		
		}else if(likeCheckData.get("mydislikecheck").equals("Y")){
			dislikeDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));		
		}		

		//tbl_like 테이블 입력/수정
		LikeEntity likeCheckView = service.likeCheckView(seqno,email);
		if(likeCheckView == null) //좋아요/싫어요를 등록한 적이 없음 
				service.likeCheckRegistry(seqno, email, mylikecheck,
						mydislikecheck, likeDate, dislikeDate);
			else //좋아요/싫어요를 등록한 적이 있음
				service.likeCheckUpdate(seqno, email, mylikecheck,
						mydislikecheck, likeDate, dislikeDate); 
		
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
