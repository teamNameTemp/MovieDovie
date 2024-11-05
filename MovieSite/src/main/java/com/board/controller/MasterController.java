package com.board.controller;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.board.dto.MovieDTO;
import com.board.dto.RequestDTO;
import com.board.entity.LikeEntity;
import com.board.entity.MemberEntity;
import com.board.entity.RequestEntity;
import com.board.entity.repository.MemberRepository;
import com.board.entity.repository.RentRepository;
import com.board.service.MasterService;
import com.board.service.MemberLogService;
import com.board.service.MemberService;
import com.board.service.RentService;
import com.board.util.PageUtil;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import net.bytebuddy.description.field.FieldDescription.InGenericShape;


@Controller
@AllArgsConstructor
@RequestMapping("/master")
public class MasterController {

	private final MasterService service;
	private final MemberLogService memberLogService;
	private final MemberService memberService;
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
    
    // 관리자 페이지 - 통계 : 작년 월별 대여 수 확인
    @GetMapping("/MonthlyRentLastYear")
    @ResponseBody
    public List<Map<String, Object>> getMonthlyRentLastYear() {
        // 현재 연도를 가져와서 작년 연도를 계산
        int lastYear = LocalDate.now().getYear() - 1; // 작년 연도 계산

        // 작년 연도에 대한 월별 대여 수 조회
        List<Object[]> resultsLastYear = rentService.RentCountByMonth(lastYear);
        
        List<Map<String, Object>> response = new ArrayList<>();
        
        for (Object[] result : resultsLastYear) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", result[0]); // 대여 날짜
            map.put("count", result[1] != null ? result[1] : 0); // 대여 수 (null 처리)
            response.add(map);
        }
        
        return response; // JSON 형태로 반환
    }
    
    // 관리자 페이지 - 통계 : 올해 월별 대여 수 확인
    @GetMapping("/MonthlyRentCurrentYear")
    @ResponseBody
    public List<Map<String, Object>> getMonthlyRentCurrentYear() {

        int currentYear = LocalDate.now().getYear(); // 올해 연도 계산

        // 올해 연도에 대한 월별 대여 수 조회
        List<Object[]> resultsCurrentYear = rentService.RentCountByMonth(currentYear);
        
        List<Map<String, Object>> response = new ArrayList<>();
        
        for (Object[] result : resultsCurrentYear) {
            Map<String, Object> map = new HashMap<>();
            map.put("date", result[0]); // 대여 날짜
            map.put("count", result[1] != null ? result[1] : 0); // 대여 수 (null 처리)
            response.add(map);
        }
        
        return response; // JSON 형태로 반환
    }
    
    //관리자 페이지 - 통계 : 올해 대여 status 수 확인
    @GetMapping("/rentStatus")
    @ResponseBody
    public List<Map<String, Object>> GetRentStatus(){
    	
    	List<Object[]> rentStatus = rentService.RentStatus();
    	List<Map<String, Object>> response = new ArrayList<>();
    	
    	for(Object[] result : rentStatus) {
    		Map<String, Object> map = new HashMap<>();
    		map.put("status", result[0]); //대여 상태 (Y 혹은 N)
    		map.put("count", result[1] != null ? result[1] : 0); //count
    		response.add(map);
    		// 추가 로그
            System.out.println("Status: " + result[0] + ", Count: " + result[1]);
    	}
    	return response;
    }
    
    //관리자 페이지 - 통계 : 올해 대여 TOP 회원 확인
    @GetMapping("/topRenters")
    @ResponseBody
    public List<Map<String, Object>> getTopRenters() {
        List<Object[]> list = rentService.findTopRenters();
        
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] item : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("email", item[0]);
            map.put("rentCount", item[1]);
            response.add(map);
        }
        
        return response; // JSON 형태로 반환
    }

	//관리자 페이지 - 통계 : 회원 가입한 날 확인
    @GetMapping("/signupResult")
    @ResponseBody
    public Map<String, Integer> getSignupResult() {

        Map<String, Integer> response = new HashMap<>();
        List<Object[]> results = memberService.signupResult();

        if (!results.isEmpty()) {
            Object[] counts = results.get(0);
            response.put("yesterdaySignup", ((Number) counts[0]).intValue());
            response.put("todaySignup", ((Number) counts[1]).intValue());
            response.put("totalMember", ((Number) counts[2]).intValue());
        } else {
            response.put("yesterdaySignup", 0);
            response.put("todaySignup", 0);
            response.put("totalMember", 0);
        }
        return response;
    }
    
    //관리자 페이지 - 통계 : 방문 수 확인
    @GetMapping("/visitResult")
    @ResponseBody
    public Map<String, Integer> getVisitResult() {

        Map<String, Integer> response = new HashMap<>();
        List<Object[]> results = memberLogService.visitResult();

        if (!results.isEmpty()) {
            Object[] counts = results.get(0);
            response.put("yesterdayVisitors", ((Number) counts[0]).intValue());
            response.put("todayVisitors", ((Number) counts[1]).intValue());
            response.put("totalVisitors", ((Number) counts[2]).intValue());
        } else {
            response.put("yesterdayVisitors", 0);
            response.put("todayVisitors", 0);
            response.put("totalVisitors", 0);
        }
        return response;
    }
    
	
	
	@GetMapping("/movieStats")
	public void getMovieStats() {
	}
	
	@GetMapping("/movieUpload")
	public void getMovieUpload() {
	}
	
	/*
	//관리자 직접 영화 업로드
	@ResponseBody
	@PostMapping("/movieUpload")
	public Map<String, String> postMovieUpload(MovieDTO movie, @RequestParam("fileUpload") MultipartFile mpr) throws Exception {
	
		//OS에 따라 이미지 저장될 디렉토리 구조 설정 시작
		String os = System.getProperty("os.name").toLowerCase();
		String path;
		if(os.contains("win"))
			path = "c:\\Repository\\profile\\";
		else 
			path = "/home/jiho/Repository/profile";
		
		//디렉토리가 존재하는지 체크해서 없다면 생성
		File p = new File(path);
		if(!p.exists()) p.mkdir();
		//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 종료
		
		File targetFile = null;
		String imgPath = "";
		
		if(!mpr.isEmpty()) {
			imgPath = mpr.getOriginalFilename();
			try {
				targetFile = new File(path + imgPath);				
				mpr.transferTo(targetFile);
				movie.setImgPath(imgPath);				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		service.movieUpload(movie);
		
		Map<String, String> data = new HashMap<>();
		data.put("message", "good");
		
	}
	
	
	//관리자 영화 업로드 시 영화 중복 체크
	@ResponseBody
	@PostMapping("/movieCheck")
	public int postMovieCheck(@RequestBody String movieNm) throws Exception {
		return service.movieCheck(movieNm);
	}
	*/
	
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
        int count = 700; //원하는 대로 수 변경 가능
    	service.createDummyRents(count);
        return " ▶▷▶ " + count + "개의 더미 대여 현황이 생성되었습니다.";
    }
		
	//Dummy 좋아요 싫어요 만들기
	@ResponseBody
    @PostMapping("/createDummyLikes")
    public String createDummyLikes() {
        int count = 500; //원하는 대로 수 변경 가능
    	service.createDummyLikes(count);
        return " ▶▷▶ " + count + "개의 더미 좋아요/싫어요 현황이 생성되었습니다.";
    }
	
	//Dummy 회원 로그 기록 만들기
	@ResponseBody
    @PostMapping("/createDummyLogs")
    public String createDummyLogs() {
        int count = 500; //원하는 대로 수 변경 가능
    	service.createDummyLogs(count);
        return " ▶▷▶ " + count + "개의 더미 LOGIN/LOGOUT 현황이 생성되었습니다.";
    }
	
	//Dummy 시청여부 만들기
	@ResponseBody
    @PostMapping("/createDummyWatch")
    public String createDummyWatch() {
        int count = 500; //원하는 대로 수 변경 가능
    	service.createDummyWatch(count);
        return " ▶▷▶ " + count + "개의 더미 시청여부가 생성되었습니다.";
    }
	
	//Dummy 즐겨찾기 만들기
	@ResponseBody
    @PostMapping("/createDummyBookmarks")
    public String createDummyBookmarks() {
        int count = 500; //원하는 대로 수 변경 가능
    	service.createDummyBookmarks(count);
        return " ▶▷▶ " + count + "개의 더미 즐겨찾기가 생성되었습니다.";
    }

}
