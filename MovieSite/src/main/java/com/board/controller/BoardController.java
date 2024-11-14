package com.board.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.border.TitledBorder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.DtoInstantiatingConverter;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.dto.MemberDTO;
import com.board.dto.MovieDTO;
import com.board.dto.ReplyInterface;
import com.board.dto.RequestDTO;
import com.board.dto.RequestReplyDTO;
import com.board.entity.LikeEntity;
import com.board.entity.NotificationEntity;
import com.board.entity.RequestEntity;
import com.board.entity.RequestReplyEntity;
import com.board.entity.repository.MemberRepository;
import com.board.service.BoardService;
import com.board.service.MasterService;
import com.board.service.MemberService;
import com.board.service.NotificationService;
import com.board.service.RequestReplyService;
import com.board.service.RequestService;
import com.board.util.PageUtil;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
@RequestMapping("/board")
public class BoardController { 
	
	private final BoardService service;
	private final MemberService memberService;
	private final MasterService masterService;
	private final RequestService requestService;
	private final RequestReplyService requestReplyService;
	private final NotificationService notificationService;
	
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
	//1. @PathVariable
	//@GetMapping('/movieDetail/title/{name}
	//=>public void getView(@PathVariable("name") String name
	
	//2. @RequestParam
	//movieDetail?title=""
	//public void getView(Model model, @RequestParam("title") String title){
	//MovieEntity entity = service.getMovieInfo(title);
	//model.addAttribute("movieInfo",entity);
	//}
	@GetMapping("/movieDetail")
	public void getView(@RequestParam("title") String title,Model model) throws Exception {
//
//		//세션 email값 가져 오기
//		String sessionEmail = (String)session.getAttribute("email");
//		
//		//좋아요/싫어요 처리
//		LikeEntity likeCheckView = service.likeCheckView(seqno,sessionEmail);
//		
//		//초기에 좋아요/싫어요 등록이 안되어져 있을 경우 "N"으로 초기화
//		if(likeCheckView == null) {
//			model.addAttribute("LikeCheck", "N");
//			model.addAttribute("DislikeCheck", "N");
//		} else if(likeCheckView != null) {
//			model.addAttribute("LikeCheck", likeCheckView.getLikecheck());
//			model.addAttribute("DislikeCheck", likeCheckView.getDislikecheck()); 
//		}
//		
//		model.addAttribute("view", service.view(seqno));
//		model.addAttribute("page", pageNum);
//		model.addAttribute("keyword", keyword);	
	}
	

	//좋아요/싫어요 관리
	@ResponseBody
	@PostMapping("/likeCheck")
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
	@PostMapping("/reply")
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
	
	//요청사항 작성하기
	@GetMapping("/requestWrite")
	public void getRequest(Model model, HttpSession session) {
		
		//세션 이메일 값 가져오기
		String SessionEmail = (String)session.getAttribute("email"); //현재 로그인 중인 사람의 이메일
		model.addAttribute("email", SessionEmail);
		
		//이메일로 회원 정보 가져오기
		MemberDTO memberDTO = memberService.memberInfo(SessionEmail);
		model.addAttribute("nickname", memberDTO.getNickname());
		
		model.addAttribute("maxSeqno", masterService.getMaxSeqno(SessionEmail));

	}
	
	//요청사항 DB 업로드
	@ResponseBody
	@PostMapping("/requestWritePost")
	public Map<String, String> postRequest(@RequestBody RequestDTO dto, HttpSession session) throws Exception {

		Map<String, String> response = new HashMap<>();
		
	    try {
	        requestService.registerRequest(dto);
	        response.put("message", "good");
	    } catch (Exception e) {
	        response.put("message", "bad");
	        e.printStackTrace(); 
	    }
		return response;
	}
	
	//회원이 자신의 요청 목록 보기
	@GetMapping("/requestList")
	public void getMemberRequest(Model model, @RequestParam(name="page", defaultValue = "1") int pageNum, 
				@RequestParam(name="keyword", defaultValue="", required=false) String keyword, HttpSession session) throws Exception {
		
		int postNum = 10; //한 화면에 보여지는 게시물 행의 갯수
		int pageListCount = 10; //화면 하단에 보여지는 페이지리스트의 페이지 갯수	
		
		
		String SessionEmail = (String)session.getAttribute("email"); //현재 로그인 중인 사람의 이메일
		model.addAttribute("email", SessionEmail);

		//이메일로 회원 정보 가져오기
		MemberDTO memberDTO = memberService.memberInfo(SessionEmail);
		model.addAttribute("nickname", memberDTO.getNickname());
		
		PageUtil page = new PageUtil();

		
		Page<RequestEntity> list = requestService.mylist(pageNum, postNum, keyword, SessionEmail);
		int totalCount = (int)list.getTotalElements();
		model.addAttribute("list", list);
		model.addAttribute("listIsEmpty", list.hasContent()?"N":"Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageList", page.getBoardRequestPageList(pageNum, postNum, pageListCount,totalCount,keyword));
	}
	
	
	//회원이 자신의 요청사항 상세보기
	@GetMapping("/requestView")
	public void getRequestView(Model model, @RequestParam(name="seqno", required = false) Long seqno, @RequestParam("page") int pageNum,
			@RequestParam(name="keyword", defaultValue = "", required = false) String keyword,
			HttpSession session) {
		
		//세션 이메일 값 가져오기
		String SessionEmail = (String)session.getAttribute("email"); //현재 로그인 중인 사람의 이메일
		model.addAttribute("email", SessionEmail);
		
		//이메일로 회원 정보 가져오기
		MemberDTO memberDTO = memberService.memberInfo(SessionEmail);
		model.addAttribute("nickname", memberDTO.getNickname());
	
		model.addAttribute("view", masterService.view(seqno));
		model.addAttribute("status", masterService.statusCheck(seqno));
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pre_seqno", requestService.my_pre_seqno(seqno, SessionEmail, keyword));
		model.addAttribute("next_seqno", requestService.my_next_seqno(seqno, SessionEmail, keyword));
	}
	
	//회원이 자신의 요청 게시물 삭제
	@Transactional
	@GetMapping("/requestDelete")
	public String getDelete(@RequestParam("seqno") Long seqno) throws Exception {
		
		//tbl_request에서 게시물을 삭제
		masterService.delete(seqno);
		return "redirect:/board/requestList";  // 삭제 후 목록 페이지로 리다이렉션
	}
	
	//댓글 처리
	@ResponseBody
	@PostMapping("/requestReply")
	public List<Map<String, Object>> postReply(@RequestBody RequestReplyDTO dto, @RequestParam("kind") String kind) 
			throws Exception{
		
		List<RequestReplyEntity> replyDataList;
		
		 switch(kind) {
	        case "L":
	            replyDataList = requestReplyService.replyList(dto.getReqseqno()); // 댓글 목록 보기
	            break;
	        case "I":
	            requestReplyService.replyRegister(dto); // 댓글 등록
	            replyDataList = requestReplyService.replyList(dto.getReqseqno()); // 등록 후 댓글 목록 반환
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid kind value: " + kind); // 잘못된 kind 값 처리
	    }
		
		 
		// RequestReplyEntity 리스트를 Map<String, Object> 리스트로 변환
	    List<Map<String, Object>> result = new ArrayList<>();
	    for (RequestReplyEntity replyData : replyDataList) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("seqno", replyData.getSeqno()); // 댓글 seqno
	        map.put("email", replyData.getEmail()); // 댓글 email
	        map.put("reqseqno", replyData.getReqseqno()); // Request Seqno
	        map.put("replywriter", replyData.getReplywriter()); 
	        map.put("replycontent", replyData.getReplycontent()); 
	        map.put("replyregdate", replyData.getReplyregdate()); 

	        result.add(map);
	    }

	    return result;
	}

	
}
