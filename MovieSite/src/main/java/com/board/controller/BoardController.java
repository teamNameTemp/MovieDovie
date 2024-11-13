package com.board.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.dto.ReplyInterface;
import com.board.dto.movie.MovieDTO;
import com.board.entity.movie.MovieEntity;
import com.board.service.BoardService;
import com.board.service.MovieAPIServiceImpl;
import com.board.service.TmdbAPIServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
@Log4j2
public class BoardController {
	private final MovieAPIServiceImpl movie;
	private final BoardService service;
	private final TmdbAPIServiceImpl tmdb;
	
	// 추천 장르
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
	
//	public void getmovie(@RequestParam("seqno") Long seqno, @RequestParam("page") int pageNum,
//			@RequestParam(name="keyword",defaultValue="",required=false) String keyword,
//			Model model, HttpSession session) throws Exception {
//		model.addAttribute("movie",movieService.getMovieInfo("20192206").block());
//		//세션 email값 가져 오기
////		String sessionEmail = (String)session.getAttribute("email");
////		
////		//좋아요/싫어요 처리
////		LikeEntity likeCheckmovie = service.likeCheckmovie(seqno,sessionEmail);
////		
////		//초기에 좋아요/싫어요 등록이 안되어져 있을 경우 "N"으로 초기화
////		if(likeCheckmovie == null) {
////			model.addAttribute("myLikeCheck", "N");
////			model.addAttribute("myDislikeCheck", "N");
////		} else if(likeCheckmovie != null) {
////			model.addAttribute("myLikeCheck", likeCheckmovie.getLikecheck());
////			model.addAttribute("myDislikeCheck", likeCheckmovie.getDislikecheck()); 
////		}
//		
////		model.addAttribute("movie", service.movie(seqno));
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
//		LikeEntity likeCheckmovie = service.likeCheckmovie(seqno,email);
//		if(likeCheckmovie == null) //좋아요/싫어요를 등록한 적이 없음 
//				service.likeCheckRegistry(seqno, email, mylikecheck,
//						mydislikecheck, likeDate, dislikeDate);
//			else //좋아요/싫어요를 등록한 적이 있음
//				service.likeCheckUpdate(seqno, email, mylikecheck,
//						mydislikecheck, likeDate, dislikeDate); 
//		
//		long likeCnt = service.movie(seqno).getLikecnt();
//		long dislikeCnt = service.movie(seqno).getDislikecnt();
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

	@GetMapping("/tmdb")
	public void getTmdb(Model model) {
		//한글로 뽑고 영어로 뽑고 해야함 onErrorResume(en-US / ko-KR)
		List<MovieDTO> trendList = movie.getTrendingMovies("day", 1, "ko-KR").block();
		List<MovieDTO> info = new ArrayList<>();
		int idx=5;
		for(MovieDTO d : trendList) {
			if(idx==0)break;
			MovieDTO dto = movie.getMovieDetails(d.getId(),"ko-KR").block();
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
