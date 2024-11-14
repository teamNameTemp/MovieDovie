package com.board.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.swing.border.TitledBorder;

import org.hibernate.bytecode.internal.bytebuddy.PrivateAccessorException;
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
import com.board.dto.RequestDTO;
import com.board.dto.RequestReplyDTO;
import com.board.entity.RequestEntity;
import com.board.entity.RequestReplyEntity;
import com.board.entity.repository.MemberRepository;
import com.board.entity.repository.RequestRepository;
import com.board.service.FavGenreService;
import com.board.service.LikeService;
import com.board.service.MasterService;
import com.board.service.MemberLogService;
import com.board.service.MemberService;
import com.board.service.NotificationService;
import com.board.service.RentService;
import com.board.service.RequestReplyService;
import com.board.service.RequestService;
import com.board.util.PageUtil;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
@RequestMapping("/master")
public class MasterController {

	private final MasterService service;
	private final LikeService likeService;
	private final MemberLogService memberLogService;
	private final MemberService memberService;
	private final RentService rentService;
	private final FavGenreService favGenreService;
	private final RequestService requestService;
	private final RequestReplyService requestReplyService;
	private final NotificationService notificationService;
	private final MemberRepository memberRepository;

	
	//회원의 요청 목록 보기
	@GetMapping("/memberRequest")
	public void getMemberRequest(Model model, @RequestParam(name="page", defaultValue = "1") int pageNum, 
				@RequestParam(name="keyword", defaultValue="", required=false) String keyword) throws Exception {
		
		int postNum = 15; //한 화면에 보여지는 게시물 행의 갯수
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
				@RequestParam(name="keyword", defaultValue = "", required = false) String keyword,
				HttpSession session) throws Exception {
	
		//세션 이메일 값 가져오기
		String SessionEmail = (String)session.getAttribute("email"); //현재 로그인 중인 사람의 이메일
		model.addAttribute("email", SessionEmail);
		
		//이메일로 회원 정보 가져오기
		MemberDTO memberDTO = memberService.memberInfo(SessionEmail);
		model.addAttribute("nickname", memberDTO.getNickname());
		
		model.addAttribute("view", service.view(seqno));
		model.addAttribute("status", service.statusCheck(seqno));
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pre_seqno", service.pre_seqno(seqno, keyword));
		model.addAttribute("next_seqno", service.next_seqno(seqno, keyword));
	}
	
	//회원 요청사항 상태 관리 -> 상태 처리 완료로 바꾸기 혹은 처리중으로 바꾸기
	@ResponseBody
	@PostMapping("/updateStatus")
	public void postUpdateStatus(@RequestBody Map<String,Object> statusCheckData, HttpSession session) throws Exception {
		
		Long seqno = ((Number) statusCheckData.get("seqno")).longValue();
		String status = (String)statusCheckData.get("status");
		
		List<RequestDTO> requestList = requestService.findBySeqno(seqno);
		
		RequestDTO requestDTO = requestList.get(0);
	    String title = requestDTO.getTitle();
	    String recieverEmail = requestDTO.getEmail();
		
		//tbl_request 테이블에 status 업데이트
		service.updateStatus(seqno, status);
		
		
		//회원에게 알람 보내기
		String content = "'" + title + "'상태가 변경되었습니다.";
		String type = "completed";

		notificationService.newNotiForUser(recieverEmail, content, type, seqno);
	}
	
	
	//회원 닉네임 찾기
	@GetMapping("/findNickname")
	@ResponseBody
	public Map<String, String> getFindNickname(@RequestParam("email") String email) throws Exception {
	    
		// 이메일로 회원 정보 가져오기
	    MemberDTO memberDTO = memberService.memberInfo(email);

	    // requestNickname을 JSON 응답으로 반환
	    Map<String, String> response = new HashMap<>();
	    response.put("requestNickname", memberDTO.getNickname());
	    return response;
	}
		

	//회원 요청 게시물 삭제
	@Transactional
	@GetMapping("/memberRequestDelete")
	public String getDelete(@RequestParam("seqno") Long seqno) throws Exception {
		
		//tbl_request에서 게시물을 삭제
		service.delete(seqno);
		return "redirect:/master/memberRequest?page=1";
	}
	
	

	//댓글 처리
	@ResponseBody
	@PostMapping("/requestReply")
	public List<Map<String, Object>> postReply(@RequestBody RequestReplyDTO dto, @RequestParam("kind") String kind, HttpSession session) 
			throws Exception{
		
		List<RequestReplyEntity> replyDataList;
		
		 switch(kind) {
	        case "L":
	            replyDataList = requestReplyService.replyList(dto.getReqseqno()); // 댓글 목록 보기
	            break;
	        case "I":
	            requestReplyService.replyRegister(dto); // 댓글 등록
	            replyDataList = requestReplyService.replyList(dto.getReqseqno()); // 등록 후 댓글 목록 반환
	            
	            //댓글 등록 알람 전달
	            String masterNickname = (String)session.getAttribute("nickname");
	            List<RequestDTO> requestList = requestService.findBySeqno(dto.getReqseqno());
	           
                RequestDTO requestDTO = requestList.get(0);
                String title = requestDTO.getTitle();
                String recieverEmail = requestDTO.getEmail();
                Long seqno = requestDTO.getSeqno();
            
	    		String content = masterNickname + "님이 '" + title + "'에 댓글을 달았습니다.";
	    		String type = "reply";
	    		notificationService.newNotiForUser(recieverEmail, content, type, seqno);
	           
	            break;
	        case "U":
	            requestReplyService.replyUpdate(dto); // 댓글 수정
	            replyDataList = requestReplyService.replyList(dto.getReqseqno()); // 수정 후 댓글 목록 반환
	            break;
	        case "D":
	            requestReplyService.replyDelete(dto.getSeqno()); // 댓글 삭제
	            replyDataList = requestReplyService.replyList(dto.getReqseqno()); // 삭제 후 댓글 목록 반환
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
    
    //관리자 페이지 - 통계 : 회원 요청사항 처리 상태 확인하기
    @GetMapping("/requestStatus")
    @ResponseBody
    public Map<String, Integer> getRequestStatus() {

        Map<String, Integer> response = new HashMap<>();
        List<Object[]> results = requestService.findStatus();

        if (!results.isEmpty()) {
            for (Object[] counts : results) {
                String status = (String) counts[0]; // 상태 (예: PENDING, COMPLETED)
                Integer count = ((Number) counts[1]).intValue(); // 해당 상태의 카운트

                // 상태별로 count를 Map에 추가
                response.put(status, count);
            }
        } else {
            // 상태별 카운트가 없는 경우 기본값 설정
            response.put("PENDING", 0);
            response.put("COMPLETED", 0);
        }
        return response;
    }
    
    //관리자 페이지 - 통계 : 회원들이 가장 좋아하는 장르 인기 순으로 보여주기
    @GetMapping("/topFavGenre")
    @ResponseBody
    public List<Map<String, Object>> getTopFavGenre(){
    	
    	List<Object[]> list = favGenreService.favGenreResult();
    	
        // 만약 리스트가 비어있다면 빈 리스트를 반환하거나 기본 값을 넣어줄 수 있음
        if (list.isEmpty()) {
            Map<String, Object> defaultMap = new HashMap<>();
            defaultMap.put("genre", "없음");
            defaultMap.put("count", 0);
            
            List<Map<String, Object>> response = new ArrayList<>();
            response.add(defaultMap); // 기본 데이터 추가
            return response;
        }
        
        // 리스트에 데이터가 있으면 정상 처리
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] item : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("genre", item[0]);
            map.put("count", item[1]);
            response.add(map);
        }
        return response;
    }

	
    //관리자 페이지 - 통계 : 좋아요 % 높은 영화 순위 보여주기
    @GetMapping("/topLikeMovie")
    @ResponseBody
    public List<Map<String, Object>> getTopLikeMovie(){
    	
    	List<Object[]> list = likeService.topLikeMovie();
    	
    	List<Map<String, Object>> response = new ArrayList<>();
    	
    	for(Object[] item : list) {
    		Map<String, Object> map = new HashMap<>();
    		map.put("movieseqno", item[0]);
    		map.put("totalCount", item[1]);
    		map.put("likeCount", item[2]);
    		map.put("likePercentage", item[3]);
    		response.add(map);
    	}
    	return response;
    }
    
    //관리자 페이지 - 통계 : 싫어요 % 높은 영화 순위 보여주기
    @GetMapping("/topDislikeMovie")
    @ResponseBody
    public List<Map<String, Object>> getTopDislikeMovie(){
    	
    	List<Object[]> list = likeService.topDislikeMovie();
    	
    	List<Map<String, Object>> response = new ArrayList<>();
    	
    	for(Object[] item : list) {
    		Map<String, Object> map = new HashMap<>();
    		map.put("movieseqno", item[0]);
    		map.put("totalCount", item[1]);
    		map.put("dislikeCount", item[2]);
    		map.put("dislikePercentage", item[3]);
    		response.add(map);
    	}
    	return response;
    }
    
    //관리자 페이지 - 통계 : 호불호 높은 영화 순위 보여주기
    @GetMapping("/topDivisiveMovie")
    @ResponseBody
    public List<Map<String, Object>> getTopDivisiveMovie(){
    	
    	List<Object[]> list = likeService.topDivisiveMovie();
    	
    	List<Map<String, Object>> response = new ArrayList<>();
    	
    	for(Object[] item : list) {
    		Map<String, Object> map = new HashMap<>();
    		map.put("movieseqno", item[0]);
    		map.put("totalCount", item[1]);
    		map.put("likeCount", item[2]);
    		map.put("dislikeCount", item[3]);
    		map.put("likePercentage", item[4]);
    		map.put("dislikePercentage", item[5]);
    		
    		// item[4]와 item[5]가 BigDecimal타입이라 BigDecimal을 String으로 변환 (소수점 아예 없게.)
            String likePercentage = ((BigDecimal)item[4]).setScale(0, RoundingMode.HALF_UP).toString();
            String dislikePercentage = ((BigDecimal)item[5]).setScale(0, RoundingMode.HALF_UP).toString();
            
            // 비율 합성
            String likeVsDislikeRatio = likePercentage + " : " + dislikePercentage;
            map.put("ratio", likeVsDislikeRatio);
    		
    		response.add(map);
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
	@ResponseBody //JSON 형식으로 응답을 반환할 때 사용.
	@PostMapping("/movieUpload")
	public Map<String, String> postMovieUpload(
			@RequestParam("posterUpload") MultipartFile poster,
    		@RequestParam("backgroundUpload") MultipartFile background,
		    @RequestParam("title") String title,
		    @RequestParam("originalTitle") String originalTitle,
		    @RequestParam("releaseDate") String releaseDate,
		    @RequestParam("originalLanguage") String originalLanguage,
		    @RequestParam("runtime") String runtime,
		    @RequestParam("overview") String overview, 
		    @RequestParam("certification") String certification,
		    
		    @RequestParam("genres") List<String> genres,
		    @RequestParam("productionCompanies") String productionCompanies, // 쉼표로 구분된 제작사들
		    @RequestParam("productionCountries") String productionCountries, // 쉼표로 구분된 제작국가들
		    @RequestParam("spokenLanguages") String spokenLanguages, // 쉼표로 구분된 언어들
		    
	?	    @RequestParam("actors") String actors, // 쉼표로 구분된 배우들
	?	    @RequestParam("directors") String directors // 쉼표로 구분된 감독들
 
			) throws Exception {
		
		KobisMovieDTO dto = new KobisMovieDTO();
	
		//여기가 이상함.
		dto.getMovieInfoResult().getMovieInfo().setMovieNm(movieNmEn);
		dto.getMovieInfoResult().getMovieInfo().setMovieNmEn(movieNmEn);
		dto.getMovieInfoResult().getMovieInfo().setPrdtYear(prdtYear);
		dto.getMovieInfoResult().getMovieInfo().setOpenDt(openDt);
		dto.getMovieInfoResult().getMovieInfo().setShowTm(showtime);
		dto.getMovieInfoResult().getMovieInfo().getAudits().get(0).setWatchGradeNm(watchGradeNm);
		dto.getMovieInfoResult().getMovieInfo().setOverview(overview);
		dto.setUploadCheck(uploadCheck);
		
		dto.getMovieInfoResult().getMovieInfo().setActors(DtoToEntityMapper.convertToActorList(actors));
		dto.getMovieInfoResult().getMovieInfo().setDirectors(DtoToEntityMapper.convertToDirectorList(directors));
		dto.getMovieInfoResult().getMovieInfo().setCompanys(DtoToEntityMapper.convertToCompanyList(companys));
		dto.getMovieInfoResult().getMovieInfo().setNations(DtoToEntityMapper.convertToNationList(nations));
		
		 // 변환 후 장르 설정
	    dto.getMovieInfoResult().getMovieInfo().setGenres(DtoToEntityMapper.convertToGenreList(genres));

		
		//OS에 따라 이미지 저장될 디렉토리 구조 설정 시작
		String os = System.getProperty("os.name").toLowerCase();
		String path = os.contains("win") ? "c:\\Repository\\profile\\":"/home/jiho/Repository/profile";
		File dir = new File(path);
    	if (!dir.exists()) dir.mkdir();
		
		// 포스터 이미지 저장
	    if (!poster.isEmpty()) {
	        String posterPath = path + poster.getOriginalFilename();
	        poster.transferTo(new File(posterPath));
	        dto.setPosterPath(posterPath);
	    }
	
	    // 백그라운드 이미지 저장
	    if (!background.isEmpty()) {
	        String backgroundPath = path + background.getOriginalFilename();
	        background.transferTo(new File(backgroundPath));
	        dto.setBackgroundPath(backgroundPath);
	    }
	
	    // 서비스 호출하여 영화 정보 저장
	    service.movieUpload(dto);
	    Map<String, String> data = new HashMap<>();
	    data.put("message", "good");
	    return data;
	}
	
	public class DtoToEntityMapper {

	    // 쉼표로 구분된 문자열을 받아서 List<Actor>로 변환하는 메소드
	    public static List<Actor> convertToActorList(String actors) {
	        List<Actor> actorList = new ArrayList<>();
	        
	        // 쉼표로 구분된 데이터 (공백도 제거하고)로 나눔
	        String[] actorArray = actors.split("\\s*,\\s*");  // 쉼표 뒤에 공백 제거
	        
	        for (String actorString : actorArray) {
	            Actor actor = new Actor();
	            actor.setPeopleNm(actorString);  // 일단 단순하게 이름만 저장
	            actor.setPeopleNmEn(null);  
	            actor.setCast(null);  
	            actor.setCastEn(null);  

	            actorList.add(actor);
	        }
	        
	        return actorList;
	    }

	    // Nations, Companys, Directors도 비슷하게 처리할 수 있음
	    public static List<Nation> convertToNationList(String nations) {
	        List<Nation> nationList = new ArrayList<>();
	        String[] nationArray = nations.split("\\s*,\\s*");
	        
	        for (String nationString : nationArray) {
	            Nation nation = new Nation();
	            nation.setNationNm(nationString);  // 국가 이름을 저장
	            nationList.add(nation);
	        }

	        return nationList;
	    }

	    public static List<Company> convertToCompanyList(String companies) {
	        List<Company> companyList = new ArrayList<>();
	        String[] companyArray = companies.split("\\s*,\\s*");
	        
	        for (String companyString : companyArray) {
	            Company company = new Company();
	            company.setCompanyNm(companyString);  // 회사 이름 저장
	            company.setCompanyNmEn(null);
	            company.setCompanyCd(null);
	            company.setCompanyPartNm(null);
	            companyList.add(company);
	        }

	        return companyList;
	    }

	    public static List<Director> convertToDirectorList(String directors) {
	        List<Director> directorList = new ArrayList<>();
	        String[] directorArray = directors.split("\\s*,\\s*");
	        
	        for (String directorString : directorArray) {
	            Director director = new Director();
	            director.setPeopleNm(directorString);  // 감독 이름 저장
	            director.setPeopleNmEn(null);
	            directorList.add(director);
	        }

	        return directorList;
	    }
	    
	    // 장르 변환 메서드
	    public static List<KobisMovieDTO.MovieInfo.Genre> convertToGenreList(List<String> genres) {
	        List<KobisMovieDTO.MovieInfo.Genre> genreList = new ArrayList<>();
	        for (String genreString : genres) {
	            KobisMovieDTO.MovieInfo.Genre genre = new KobisMovieDTO.MovieInfo.Genre();
	            genre.setGenreNm(genreString);  // 장르 이름을 설정
	            genreList.add(genre);
	        }
	        return genreList;
	    }
	    
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
	
	//Dummy 좋아하는 장르 만들기
	@ResponseBody
    @PostMapping("/createDummyFavGenre")
    public String createDummyFavGenre() {
        int count = 500; //원하는 대로 수 변경 가능
    	service.createDummyFavGenre(count);
        return " ▶▷▶ " + count + "개의 더미 좋아하는 장르가 생성되었습니다.";
    }

}
