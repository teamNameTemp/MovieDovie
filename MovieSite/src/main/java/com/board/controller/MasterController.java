package com.board.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

import com.board.dto.RequestDTO;
import com.board.entity.LikeEntity;
import com.board.entity.MemberEntity;
import com.board.entity.RequestEntity;
import com.board.entity.repository.MemberRepository;
import com.board.entity.repository.RentRepository;
import com.board.service.MasterService;
import com.board.service.RentService;
import com.board.util.PageUtil;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
@RequestMapping("/master")
public class MasterController {

	private final MasterService service;
	private final RentService rentService;

	
	//회원의 요청 목록 보기
	@GetMapping("/memberRequest")
	public void getMemberRequest(Model model, @RequestParam(name="page", defaultValue = "1") int pageNum, 
				@RequestParam(name="keyword", defaultValue="", required=false) String keyword) throws Exception {
		
		int postNum = 10; //한 화면에 보여지는 게시물 행의 갯수
		int pageListCount = 10; //화면 하단에 보여지는 페이지리스트의 페이지 갯수	
	
		PageUtil page = new PageUtil();
		Page<RequestEntity> list = service.list(pageNum, postNum, keyword);
		int totalCount = (int)list.getTotalElements();

		model.addAttribute("list", list);
		model.addAttribute("listIsEmpty", list.hasContent()?"N":"Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageList", page.getRequestPageList(pageNum, postNum, pageListCount,totalCount,keyword));
	}

	
	//회원 요청 내용 상세 보기
	@GetMapping("/memberRequestView")
	public void getMemberRequestView(Model model, @RequestParam(name="seqno", required = false) Long seqno, @RequestParam("page") int pageNum,
				@RequestParam(name="keyword", defaultValue = "", required = false) String keyword) throws Exception {
	
		model.addAttribute("view", service.view(seqno));
		model.addAttribute("status", service.statusCheck(seqno));
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pre_seqno", service.pre_seqno(seqno, keyword));
		model.addAttribute("next_seqno", service.next_seqno(seqno, keyword));
	}
	
	//회원 요청사항 상태 관리
	@ResponseBody
	@PostMapping("/updateStatus")
	public void postUpdateStatus(@RequestBody Map<String,Object> statusCheckData) throws Exception {
		
		Long seqno = ((Number) statusCheckData.get("seqno")).longValue();
		String status = (String)statusCheckData.get("status");
		
		//tbl_request 테이블에 status 업데이트
		service.updateStatus(seqno, status);
		
	}
		

	//회원 요청 게시물 삭제
	@Transactional
	@GetMapping("/memberRequestDelete")
	public String getDelete(@RequestParam("seqno") Long seqno) throws Exception {
		
		//tbl_request에서 게시물을 삭제
		service.delete(seqno);
		return "redirect:/master/memberRequest?page=1";
	}
	
	
	@GetMapping("/memberStats")
	public void getMemberStats() {
	}
	
	
	// 관리자 페이지 - 통계 : 최근 일주일 간 대여 수 확인
    @GetMapping("/weeklyRent")
    @ResponseBody
    public List<Map<String, Object>> getWeeklyRent() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime sevenDaysAgo = now.minusDays(7);
        
        List<Object[]> results = rentService.RentCount(sevenDaysAgo, now);
        List<Map<String, Object>> response = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", result[0]); // 대여 날짜
            map.put("count", result[1]); // 대여 수
            response.add(map);
        }
        
        return response; // JSON 형태로 반환
    }
	
	
	//관리자 페이지 - 통계 : 이번 연도 누적! 대여 수 확인 
	@GetMapping("/RentCumulative")
	@ResponseBody
	public List<Map<String, Object>> getRentCumulative() {
		List<Object[]> cumulativeRents = rentService.RentCumulative();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] result : cumulativeRents) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", result[0]); // 대여 날짜
            map.put("count", result[1]); // 누적 대여 수
            response.add(map);
        }
        
        return response; // JSON 형태로 반환
    }
	
	
	
	@GetMapping("/movieStats")
	public void getMovieStats() {
	}
	
	@GetMapping("/movieUpload")
	public void getMovieUpload() {
	}
	
	/*
	
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
	
	
	//더미 데이터 만들기 페이지
	@GetMapping("/dummyMaker")
	public void getDummyMaker() { }
	

	//Dummy members 만들기
	@ResponseBody
    @PostMapping("/createDummyMembers")
    public String createDummyMembers() {
        int count = 100; //원하는 대로 수 변경 가능
    	service.createDummyMembers(count);
        return " ▶▷▶ " + count + "명의 더미 회원이 생성되었습니다.";
    }
	
	//Dummy 대여현황 만들기
	@ResponseBody
    @PostMapping("/createDummyRents")
    public String createDummyRents() {
        int count = 100; //원하는 대로 수 변경 가능
    	service.createDummyRents(count);
        return " ▶▷▶ " + count + "개의 더미 대여 현황이 생성되었습니다.";
    }
		
	//Dummy 대여현황 만들기
	@ResponseBody
    @PostMapping("/createDummyLikes")
    public String createDummyLikes() {
        int count = 500; //원하는 대로 수 변경 가능
    	service.createDummyLikes(count);
        return " ▶▷▶ " + count + "개의 더미 좋아요/싫어요 현황이 생성되었습니다.";
    }
	
	//Dummy 대여현황 만들기
	@ResponseBody
    @PostMapping("/createDummyLogs")
    public String createDummyLogs() {
        int count = 500; //원하는 대로 수 변경 가능
    	service.createDummyLogs(count);
        return " ▶▷▶ " + count + "개의 더미 LOGIN/LOGOUT 현황이 생성되었습니다.";
    }
	
	//Dummy 대여현황 만들기
	@ResponseBody
    @PostMapping("/createDummyWatch")
    public String createDummyWatch() {
        int count = 100; //원하는 대로 수 변경 가능
    	service.createDummyWatch(count);
        return " ▶▷▶ " + count + "개의 더미 시청여부가 생성되었습니다.";
    }
	
	//Dummy 즐겨찾기 만들기
	@ResponseBody
    @PostMapping("/createDummyBookmarks")
    public String createDummyBookmarks() {
        int count = 200; //원하는 대로 수 변경 가능
    	service.createDummyBookmarks(count);
        return " ▶▷▶ " + count + "개의 더미 즐겨찾기가 생성되었습니다.";
    }

}
