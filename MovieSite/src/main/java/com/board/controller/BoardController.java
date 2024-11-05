package com.board.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.dto.ReplyInterface;
import com.board.entity.movie.MovieEntity;
import com.board.service.BoardService;
import com.board.service.KobisAPIService;
import com.board.service.MovieAPIService;
import com.board.service.TmdbAPIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
@Log4j2
public class BoardController {
	private final MovieAPIService movieService;
	private final BoardService service;
	private final TmdbAPIService tmdb;
	private final KobisAPIService kobis;
	
	// 추천 장르
	@GetMapping("/selectGenre")
	public void getMovieSelect() {
	}

	@GetMapping("/list")
	public void getList(Model model) throws JsonMappingException, JsonProcessingException {
		model.addAttribute("dailyMovies",movieService.getDailyBoxOffice());
	}
	// 장르 필터
	@PostMapping("/genreCheck")
	public String postGenreCheck() {
		return null;
	}
	
	
	//영화상세
	@GetMapping("/movieDetail") //?seqno=name
	public String movieDetail(@PathVariable Long seqno, @RequestParam("page") int pageNum,
			@RequestParam(name="keyword",defaultValue="",required=false) String keyword,
			Model model, HttpSession session) throws Exception {
				MovieEntity entity = movieService.getMovieByCode(seqno);

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
	
//	public void getView(@RequestParam("seqno") Long seqno, @RequestParam("page") int pageNum,
//			@RequestParam(name="keyword",defaultValue="",required=false) String keyword,
//			Model model, HttpSession session) throws Exception {
//		model.addAttribute("movie",movieService.getMovieInfo("20192206").block());
//		//세션 email값 가져 오기
////		String sessionEmail = (String)session.getAttribute("email");
////		
////		//좋아요/싫어요 처리
////		LikeEntity likeCheckView = service.likeCheckView(seqno,sessionEmail);
////		
////		//초기에 좋아요/싫어요 등록이 안되어져 있을 경우 "N"으로 초기화
////		if(likeCheckView == null) {
////			model.addAttribute("myLikeCheck", "N");
////			model.addAttribute("myDislikeCheck", "N");
////		} else if(likeCheckView != null) {
////			model.addAttribute("myLikeCheck", likeCheckView.getLikecheck());
////			model.addAttribute("myDislikeCheck", likeCheckView.getDislikecheck()); 
////		}
//		
////		model.addAttribute("view", service.view(seqno));
////		model.addAttribute("page", pageNum);
////		model.addAttribute("keyword", keyword);	
//	}
	

	//좋아요/싫어요 관리
//	@ResponseBody
//	@PostMapping("/board/likeCheck")
//	public String postLikeCheck(@RequestBody Map<String,Object> likeCheckData) throws Exception {
//		
//		int seq = (int)likeCheckData.get("seqno");
//		Long seqno = (long)seq;
//		//Long seqno = (Long)likeCheckData.get("seqno");
//		String email = (String)likeCheckData.get("email");
//		String mylikecheck = (String)likeCheckData.get("mylikecheck");
//		String mydislikecheck = (String)likeCheckData.get("mydislikecheck");
//		int checkCnt = (int)likeCheckData.get("checkCnt");		
//		
//		//현재 날짜, 시간 구해서 좋아요/싫어요 한 날짜/시간 입력 및 수정
//		String likeDate = "";
//		String dislikeDate = "";
//		LocalDateTime now = LocalDateTime.now();
//		if(likeCheckData.get("mylikecheck").equals("Y")) {
//			likeDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));		
//		}else if(likeCheckData.get("mydislikecheck").equals("Y")){
//			dislikeDate = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));		
//		}		
//
//		//tbl_like 테이블 입력/수정
//		LikeEntity likeCheckView = service.likeCheckView(seqno,email);
//		if(likeCheckView == null) //좋아요/싫어요를 등록한 적이 없음 
//				service.likeCheckRegistry(seqno, email, mylikecheck,
//						mydislikecheck, likeDate, dislikeDate);
//			else //좋아요/싫어요를 등록한 적이 있음
//				service.likeCheckUpdate(seqno, email, mylikecheck,
//						mydislikecheck, likeDate, dislikeDate); 
//		
//		long likeCnt = service.view(seqno).getLikecnt();
//		long dislikeCnt = service.view(seqno).getDislikecnt();
//		
//		switch(checkCnt) {
//		case 1: likeCnt--; break;
//		case 2: likeCnt++; dislikeCnt--; break;
//		case 3: likeCnt++; break;
//		case 4: dislikeCnt--; break;
//		case 5: likeCnt--; dislikeCnt++; break;
//		case 6: dislikeCnt++; break;
//		}
//		
//		MovieDTO board = MovieDTO.builder()
//							.seqno(seqno)
//							.likecnt(likeCnt)
//							.dislikecnt(dislikeCnt)
//							.build();
	//		service.boardLikeUpdate(board);
//		
//		return "{\"likecnt\":\"" + likeCnt + "\",\"dislikecnt\": \"" + dislikeCnt + "\"}";
//	}
	
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

//	@GetMapping("/tmdb")
//	public void getAuthentication(Model model){
//		 //tmdb.getAuthentication();
//		Mono<Map> mono = tmdb.getMovieDetails("1034541");
//		mono.subscribe(value -> System.out.println(value.get("poster_path")));
//		System.out.println(mono.block());
//		Mono<ActorDetailDTO> actors = tmdb.getActorDetails(287);
//		System.out.println("배우 : " + actors.block());
//		String posterOriginPath = "https://image.tmdb.org/t/p/original";
//
//		model.addAttribute("actor",actors.block());
//		model.addAttribute("detail",mono.block());
//		model.addAttribute("image",posterOriginPath+mono.block().get("poster_path"));
//		model.addAttribute("image",posterOriginPath+actors.block().getProfile_path());;
//	}
	
//	@GetMapping("/daily")
//	public void getDaily(@RequestParam("targetDt") String targetDate, Model model) {
//		try {
//			List<Map<String, Object>> dailyList = getDailyBoxOfficeList(targetDate);
//			if (!dailyList.isEmpty()) {
//				String movieCd = (String) dailyList.get(0).get("movieCd");
//				Mono<String> movieInfo = kobis.getMovieInfo(movieCd);
//				Map<String,Object> info =  convertToMapper.getMovieInfo(movieInfo.block());
//				model.addAttribute("movies", dailyList);
//				model.addAttribute("movieInfo", info);
//			} else {
//				// 적절한 오류 처리
//				model.addAttribute("error", "영화 목록을 찾지 못했습니다.");
//			}
//		} catch (Exception e) {
//			// 예외 로깅
//			model.addAttribute("error", "An error occurred while processing your request");
//		}
//	}

	

//	private Map<String, Object> getMovieInfo(String movieCd) throws JsonProcessingException {
//		String movieInfoResult = kobis.getMovieInfo(movieCd);
//		return convertToMapper.getMovieInfo(movieInfoResult);
//	}
}
