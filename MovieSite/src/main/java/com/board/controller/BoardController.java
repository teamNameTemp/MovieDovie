package com.board.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.board.dto.ReplyInterface;
import com.board.dto.RequestDTO;
import com.board.dto.RequestReplyDTO;
import com.board.dto.movie.MovieDTO;
import com.board.entity.RequestEntity;
import com.board.entity.RequestReplyEntity;
import com.board.service.BoardService;
import com.board.service.MasterService;
import com.board.service.MemberService;
import com.board.service.MovieAPIServiceImpl;
import com.board.service.RequestReplyService;
import com.board.service.RequestService;
import com.board.service.TmdbAPIServiceImpl;
import com.board.util.PageUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@AllArgsConstructor
@Log4j2
@RequestMapping("/board")
public class BoardController { 
	private final MovieAPIServiceImpl movie;
	private final TmdbAPIServiceImpl tmdb;
	private final BoardService service;
	private final MemberService memberService;
	private final MasterService masterService;
	private final RequestService requestService;
	private final RequestReplyService requestReplyService;
	
	//추천 장르
	@GetMapping("/selectGenre")
	public void getMovieSelect(Model model) {
		Map<String,Integer> map = tmdb.getGenreList().block();
		
		model.addAttribute("genres",map);
	}
	
	@ResponseBody
	@PostMapping("/selectGenre")
	public Map<String,Boolean> postSelectGenres(@RequestBody Map<String, List<String>> payload){
		List<String> selectedGenres = payload.get("genres");

		Map<String, Boolean> response = new HashMap<>();
		response.put("success", true);
		return response;
	}

	@GetMapping("/list")
	public void getList(Model model) throws JsonMappingException, JsonProcessingException {
		List<MovieDTO> trendList = movie.getTrendingMovies("day", 1, "ko-KR").block();
		List<MovieDTO> musicList = tmdb.getMoviesByGenreName("음악", 1).block();
//		List<MovieDTO> fantasyList = tmdb.getMoviesByGenreName("판타지", 1).block();
//		List<MovieDTO> fearList = tmdb.getMoviesByGenreName("공포", 1).block();
//		List<MovieDTO> documentaryList = tmdb.getMoviesByGenreName("다큐멘터리", 1).block();
//		List<MovieDTO> familyList = tmdb.getMoviesByGenreName("가족", 1).block();
//		List<MovieDTO> warList = tmdb.getMoviesByGenreName("전쟁", 1).block();
//		List<MovieDTO> adventureList = tmdb.getMoviesByGenreName("모험", 1).block();
//		List<MovieDTO> crimeList = tmdb.getMoviesByGenreName("범죄", 1).block();
//		List<MovieDTO> historyList = tmdb.getMoviesByGenreName("역사", 1).block();
//		List<MovieDTO> animationList = tmdb.getMoviesByGenreName("애니메이션", 1).block();
//		List<MovieDTO> thrillerList = tmdb.getMoviesByGenreName("스릴러", 1).block();
//		List<MovieDTO> mysteryList = tmdb.getMoviesByGenreName("미스터리", 1).block();
//		List<MovieDTO> actionList = tmdb.getMoviesByGenreName("액션", 1).block();
//		List<MovieDTO> comedyList = tmdb.getMoviesByGenreName("코미디", 1).block();
//		List<MovieDTO> tvMovieList = tmdb.getMoviesByGenreName("TV 영화", 1).block();
//		List<MovieDTO> romanceList = tmdb.getMoviesByGenreName("로맨스", 1).block();
//		List<MovieDTO> sfList = tmdb.getMoviesByGenreName("SF", 1).block();
//		List<MovieDTO> dramaList = tmdb.getMoviesByGenreName("드라마", 1).block();
//		List<MovieDTO> westList = tmdb.getMoviesByGenreName("서부", 1).block();
//		
		
		Map<String,Object> map = movie.getMovieDetails(trendList.get(0).getId());
		log.info(map);
		log.info(map.get("movie"));
		
		model.addAttribute("dailyMovies",trendList);
		model.addAttribute("music",musicList);
//		model.addAttribute("fantasy",fantasyList);
//		model.addAttribute("fear",fearList);
//		model.addAttribute("documentary",documentaryList);
//		model.addAttribute("family",familyList);
//		model.addAttribute("war",warList);
//		model.addAttribute("adventure",adventureList);
//		model.addAttribute("crime",crimeList);
//		model.addAttribute("history",historyList);
//		model.addAttribute("animation",animationList);
//		model.addAttribute("thriller",thrillerList);
//		model.addAttribute("mystery",mysteryList);
//		model.addAttribute("action",actionList);
//		model.addAttribute("comedy",comedyList);
//		model.addAttribute("tvMovie",tvMovieList);
//		model.addAttribute("romance",romanceList);
//		model.addAttribute("sf",sfList);
//		model.addAttribute("drama",dramaList);
//		model.addAttribute("west",westList);
		
	}
	// 장르 필터
	@PostMapping("/genreCheck")
	public String postGenreCheck() {
		return null;
	}
	
	//영화상세
	@GetMapping("/movieDetail")
	public void getView(Model model, @RequestParam("movieId") Long id) throws JsonMappingException, JsonProcessingException{
//		MovieDTO detail = tmdb.getMovieDetails(id, "ko-KR").block();
		Map<String,Object> map = movie.getMovieDetails(id);
		log.info(map);
		log.info(map.get("movie"));
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

	@GetMapping("/tmdb")
	public void getTmdb(Model model) {
		//한글로 뽑고 영어로 뽑고 해야함 onErrorResume(en-US / ko-KR)
		List<MovieDTO> trendList = movie.getTrendingMovies("day", 1, "ko-KR").block();
		List<MovieDTO> info = new ArrayList<>();
		int idx=5;
		for(MovieDTO d : trendList) {
			if(idx==0)break;
			MovieDTO dto = movie.getMovieDetails(d.getId(),"ko-KR").block();
			log.info(dto);
			dto.setTitle(d.getTitle());
			info.add(dto);
			idx--;
		}
		
		log.info(tmdb.getGenreList().block());
		List<MovieDTO> movies = tmdb.getMoviesByGenreName("액션", 1).block();
		List<MovieDTO> details = new ArrayList<>();
		for(MovieDTO d : movies) {
			MovieDTO detail = tmdb.getMovieDetails(d.getId(), "ko-KR").block();
			details.add(detail);
		}
		model.addAttribute("movies",details);
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

	
}
