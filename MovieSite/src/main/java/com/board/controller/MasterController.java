package com.board.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.entity.LikeEntity;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
@RequestMapping("/master")
public class MasterController {

	//private final MasterService service;
	
	//회원의 요청 목록 보기
	@GetMapping("/memberRequest")
	public void getMemberRequest(Model model, @RequestParam("page") int pageNum, 
				@RequestParam(name="keyword", defaultValue="", required=false) String keyword) throws Exception {
		
		int postNum = 10; //한 화면에 보여지는 게시물 행의 갯수
		int pageListCount = 10; //화면 하단에 보여지는 페이지리스트의 페이지 갯수	
	
	//	PageUtil page = new PageUtil();
	//	Page<BoardEntity> list = service.list(pageNum, postNum, keyword);
	//	int totalCount = (int)list.getTotalElements();

	//	model.addAttribute("list", list);
	//	model.addAttribute("listIsEmpty", list.hasContent()?"N":"Y");
	//	model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
	//	model.addAttribute("pageList", page.getPageList(pageNum, postNum, pageListCount,totalCount,keyword));
	}

		
	//회원 요청 내용 상세 보기
	@GetMapping("/memberRequestView")
	public void getMemberRequestView(Model model, @RequestParam("seqno") long seqno, @RequestParam("page") int pageNum,
				@RequestParam(name="keyword", defaultValue = "", required = false) String keyword, HttpSession session) throws Exception {
	
		//세션 이메일 값 가져오기 (현재 로그인 중인 사람의 이메일)
		String sessionEmailString = (String)session.getAttribute("email");
	
		//처리 완료 여부 판단
		//1. tbl_request에서 값 가져오기
		RequestEntity status = service
		
	}
	
	
	//게시물 상세 보기
		@GetMapping("/board/view")
		public void getView(Model model, @RequestParam("seqno") Long seqno, @RequestParam("page") int pageNum,
					@RequestParam(name="keyword", defaultValue = "", required=false) String keyword, HttpSession session) throws Exception {
			
			//세션 이메일 값 가져오기
			String SessionEmail = (String)session.getAttribute("email"); //현재 로그인 중인 사람의 이메일
			
			//조회수 증가
			if(!SessionEmail.equals(service.view(seqno).getEmail().getEmail())) {
				service.hitno(seqno);
			}
			
			//게시물 상세 보기 페이지를 보고 있는 사용자가 좋아요/싫어요를 체크 했는지 여부를 판단
			//1. tbl_like에서 값을 가져 온다
			LikeEntity likeCheckView = service.likeCheckView(seqno,SessionEmail);
			
			//2. 처음 읽은 값이 좋아요/싫어요 등록이 안되어져 있는 경우 "N"으로 초기화
			if(likeCheckView == null) { //한번도 이 게시물에 대해서 좋아요/싫어요를 누른 적이 없음
				model.addAttribute("myLikeCheck","N");
				model.addAttribute("myDislikeCheck", "N");
			} else if(likeCheckView != null) { //한번 이상 이 게시물에 대해서 좋아요/싫어요를 누른 적이 있음
				model.addAttribute("myLikeCheck", likeCheckView.getMylikecheck());
				model.addAttribute("myDislikeCheck", likeCheckView.getMydislikecheck());			
			}	
			
			model.addAttribute("view", service.view(seqno));
			model.addAttribute("page", pageNum);
			model.addAttribute("keyword", keyword);
			model.addAttribute("pre_seqno", service.pre_seqno(seqno, keyword));
			model.addAttribute("next_seqno", service.next_seqno(seqno, keyword));
			model.addAttribute("fileListView", service.fileListView(seqno));
		}
	
	
	@GetMapping("/memberStats")
	public void getMemberStats() {
	}
	
	@GetMapping("/movieStats")
	public void getMovieStats() {
	}
	
	@GetMapping("/movieUpload")
	public void getMovieUpload() {
	}
	
	/*
	//회원 요청사항 상태 관리
	@ResponseBody
	@PostMapping("/master/memberReqeustViewStatus")
	public String postStatusCheck(@RequestBody Map<String,Object> statusCheckData) throws Exception {
		
		long seqno = (long)statusCheckData.get("seqno");
		String email = (String)statusCheckData.get("email");
		String status = (String)statusCheckData.get("status");

		//tbl_request 테이블 입력/수정
		RequestEntity statusView = service.statusView(seqno,email);
		if(statusView == null) //상태를 등록한 적이 없음 
				service.statusRegistry(seqno, email, status);
			else //상태를 등록한 적이 있음
				service.statusUpdate(seqno, email, status); 
		
		MasterDTO master = MasterDTO.builder()
							.seqno(seqno)
							.requeststatus(requeststatus)
							.build();
		service.statusUpdate(master);
		
		return "{\"likecnt\":\"" + likeCnt + "\",\"dislikecnt\": \"" + dislikeCnt + "\"}";
	}
	
	//댓글 처리
	@ResponseBody
	@PostMapping("/master/memberRequestViewreply")
	public List<ReplyInterface> postReply(ReplyInterface reply,@RequestParam("kind") String kind) 
			throws Exception{
		
		switch(kind) {
		
		case "I": service.replyRegistry(reply); //댓글 등록
				  break;
		case "U": service.replyUpdate(reply); //댓글 수정 
			      break;
		case "D": service.replyDelete(reply); //댓글 삭제
				  break;
		}
		
		return service.replyView(reply);
	}
	*/
}
