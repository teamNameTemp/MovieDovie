package com.board.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.board.entity.movie.GenreEntity;
import com.board.service.BoardService;
import com.board.service.BookmarkService;
import com.board.service.FavGenreService;
import com.board.service.LikeService;
import com.board.service.MasterService;
import com.board.service.MemberService;
import com.board.service.MovieAPIServiceImpl;
import com.board.service.NotificationService;
import com.board.service.ReplyService;
import com.board.service.RequestReplyService;
import com.board.service.RequestService;
import com.board.service.TmdbAPIServiceImpl;
import com.board.service.WatchedService;
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
	private final FavGenreService favGenre;
	private final LikeService like;
	private final ReplyService reply;
	private final WatchedService watch;
	private final BookmarkService bookmark;
	private final NotificationService notificationService;

	@GetMapping("/list")
	public void getList(Model model, HttpSession session) throws JsonMappingException, JsonProcessingException {
		String email = (String) session.getAttribute("email");
		MemberDTO memberDTO = memberService.memberInfo(email);
		
		String nickname = (String) session.getAttribute("nickname");
		model.addAttribute("nickname", nickname);

		
		  List<MovieDTO> trend = tmdb.getTrendingMovies("day", 1, "ko-KR").block();
          List<MovieDTO> trendList = new ArrayList<>();
          
          for(MovieDTO d : trend) {
              MovieDTO dto = tmdb.getMovieDetails(d.getId(), "ko-KR").block();
              trendList.add(dto);
          }
          //트렌드 영화를 못찾을 시 예외로 한번 더 저장
          movie.saveTheDataBases(trendList);
		
		for(MovieDTO dto : trendList) {
			Map<String,Object> bookmarkMap = bookmark.getBookmarkInfo((String)session.getAttribute("email"), dto.getId());
			if(bookmarkMap != null) {
				dto.setIsBookmarked((String)bookmarkMap.get("status"));
			}
//			dto.setIsCart()
		}
		
		
		//선호 장르별 영화 리스트
		Map<String, Object> favGenreList = new HashMap<>();
		Optional<Map<String, Object>> genre = favGenre.getFavGenre();
		if (genre.isPresent()) {
			for (int i = 0; i < genre.get().size(); i++) {
				// 장르 컬럼 이름
				String genreNm = (String) genre.get().get("genre" + (i + 1));
				// 인자로 받은 장르이름을 가진 모든 장르엔티티를 가져온다.
				List<GenreEntity> genres = movie.getGenreList(genreNm);
				// 해당 장르를 포함하고 있는 영화리스트를 가져온다.
				favGenreList.put(genreNm, movie.findByGenreGetMovieList(genres));
			}
		}	
		
		//만장일치! 다들 이 영화 좋아해요
		//요즘 인기 폭팔! 댓글 많은 영화
		//이건 꼭 봐야해! 회원들이 가장 많이 본 영화
		//오늘 대여 TOP 영화
		//무비두비에 새로 올라온 영화
		
		model.addAttribute("nickname", session.getAttribute("nickname"));
		model.addAttribute("dailyMovies", trendList);
		model.addAttribute("favGenre", genre.get());
		model.addAttribute("favGenreList", favGenreList);


		model.addAttribute("role", memberDTO.getRole());

	}
	
	@GetMapping("/mypage")
	public void getMyPage() {
		
	}
	
	// 장르 필터
	@PostMapping("/genreCheck")
	public String postGenreCheck() {
		return null;
	}

	// 영화상세
	@GetMapping("/movieDetail")
	public void getView(Model model, @RequestParam("movieId") Long movieId, HttpSession session)
			throws JsonMappingException, JsonProcessingException {
		String email = (String) session.getAttribute("email");
		String nickname = (String) session.getAttribute("nickname");

		// DB에서 영화 정보 가져오기
		Map<String, Object> movieDetail = movie.getMovieDetails(movieId);
		model.addAttribute("movie", movieDetail);

		// 사용자가 로그인한 경우에만 회원 정보와 좋아요 정보를 처리
		if (email != null && !email.isEmpty()) {
			MemberDTO memberDTO = memberService.memberInfo(email);
			model.addAttribute("nickname", nickname);
			model.addAttribute("role", memberDTO.getRole());

		} else {
			// 로그인하지 않은 사용자를 위한 기본값 설정
			model.addAttribute("nickname", "Guest");
			model.addAttribute("role", "GUEST");
		}
		// 나의 movieStat 정보
		Map<String, Object> mylike = like.getLikeInfo(email, movieId);
		if (mylike == null || mylike.isEmpty()) {
			model.addAttribute("like", null);
		} else {
			model.addAttribute("like", mylike);
		}
		Map<String,Object> myWatch = watch.getWatchedInfo(email, movieId);
		if (myWatch == null || myWatch.isEmpty()) {
			model.addAttribute("watch", null);
		} else {
			model.addAttribute("watch", myWatch);
		}	
		Map<String,Object> myBookmark = bookmark.getBookmarkInfo(email, movieId);
		if (myBookmark == null || myBookmark.isEmpty()) {
			model.addAttribute("bookmark", null);
		} else {
			model.addAttribute("bookmark", myBookmark);
		}

		//movieStat 정보
		Map<String, Object> likes = like.getAllLikeInfo(movieId);
		Map<String,Object> watches = watch.getAllWatchedInfo(movieId);
		Map<String,Object> bookmarks = bookmark.getAllBookmarkInfo(movieId);

		model.addAttribute("likes", likes);
		model.addAttribute("watches",watches);
		model.addAttribute("bookmarks",bookmarks);

	}

	@PostMapping("/movieDetail/reviews")
	@ResponseBody
	public Map<String, Boolean> postMovieDetailReview(@RequestParam("movieId") Long movieId,
			@RequestParam("content") String content, HttpSession session) {

		String email = (String) session.getAttribute("email");

		Map<String, Boolean> response = new HashMap<>();

		// 리뷰 업데이트 및 저장
		reply.saveOrUpdate(content, email, movieId);

		response.put("ok", true);

		return response;
	}

	// 좋아요 싫어요 처리
	@PostMapping("/movieDetail/likes")
	@ResponseBody
	public Map<String, Object> postMovieDetailLike(@RequestParam("movieId") Long movieId,
			@RequestParam("isLike") String isLike, @RequestParam("action") String action, HttpSession session) {
		Map<String, Object> response = new HashMap<>();

		String rating = "Y".equals(isLike) ? "like" : "dislike";
		String email = (String) session.getAttribute("email");

		boolean isAdd = "add".equals(action);
		// 좋아요 싫어요 업데이트 및 저장
		try {
			like.saveOrUpdate(rating, movieId, email, isAdd);
			response.put("message", "good");
			// 모든 like 정보
			Map<String, Object> likes = like.getAllLikeInfo(movieId);
			response.put("likeCount", likes.get("likeCount"));
			response.put("dislikeCount", likes.get("dislikeCount"));
			response.put("userLike", isLike);
		} catch (Exception e) {
			log.error("likedislike 오류 : ", e);
			response.put("message", "error");
			response.put("error", e.getMessage());
		}

		return response;
	}

	// 시청 여부 처리
	@PostMapping("/movieDetail/watched")
	@ResponseBody
	public Map<String, Object> postMovieDetailWatched(@RequestParam("movieId") Long movieId,
			@RequestParam("action") String action, HttpSession session) {
		Map<String, Object> response = new HashMap<>();

		String email = (String) session.getAttribute("email");

		// 좋아요 싫어요 업데이트 및 저장
		try {
			watch.saveOrUpdate(action, movieId, email);
			response.put("message", "good");
			// 모든 like 정보
			Map<String, Object> watches = watch.getAllWatchedInfo(movieId);
			response.put("count", watches.get("count"));
		} catch (Exception e) {
			log.error("wathed 오류 : ", e);
			response.put("message", "error");
			response.put("error", e.getMessage());
		}

		return response;
	}

	// 북마크 관련 처리
	@PostMapping("/movieDetail/bookmark")
	@ResponseBody
	public Map<String, Object> postMovieDetailBookmark(@RequestParam("movieId") Long movieId,
			@RequestParam("action") String action, HttpSession session) {
		Map<String, Object> response = new HashMap<>();

		String email = (String) session.getAttribute("email");

		// 좋아요 싫어요 업데이트 및 저장
		try {
			bookmark.saveOrUpdate(action, movieId, email);
			response.put("message", "good");
			// 모든 like 정보
			Map<String, Object> bookmarks = bookmark.getAllBookmarkInfo(movieId);
			response.put("count", bookmarks.get("count"));
		} catch (Exception e) {
			log.error("bookmark 오류 : ", e);
			response.put("message", "error");
			response.put("error", e.getMessage());
		}

		return response;
	}

	// 댓글 처리
	@ResponseBody
	@PostMapping("/reply")
	public List<ReplyInterface> postReply(ReplyInterface reply, @RequestParam("option") String option)
			throws Exception {

		switch (option) {

		case "I":
			service.replyRegistry(reply); // 댓글 등록
			break;
		case "U":
			service.replyUpdate(reply); // 댓글 수정
			break;
		case "D":
			service.replyDelete(reply); // 댓글 삭제
			break;
		}

		return service.replyView(reply);
	}

	@GetMapping("/tmdb")
	public void getTmdb(Model model) {
		// 한글로 뽑고 영어로 뽑고 해야함 onErrorResume(en-US / ko-KR)
		List<MovieDTO> trendList = tmdb.getTrendingMovies("day", 1, "ko-KR").block();
		List<MovieDTO> info = new ArrayList<>();
		int idx = 5;
		for (MovieDTO d : trendList) {
			if (idx == 0)
				break;
			MovieDTO dto = tmdb.getMovieDetails(d.getId(), "ko-KR").block();
			log.info(dto);
			dto.setTitle(d.getTitle());
			info.add(dto);
			idx--;
		}

		log.info(tmdb.getGenreList().block());
		List<MovieDTO> movies = tmdb.getMoviesByGenreName("액션", 1).block();
		List<MovieDTO> details = new ArrayList<>();
		for (MovieDTO d : movies) {
			MovieDTO detail = tmdb.getMovieDetails(d.getId(), "ko-KR").block();
			details.add(detail);
		}
		model.addAttribute("movies", details);
	}

	// 요청사항 작성하기
	@GetMapping("/requestWrite")
	public void getRequest(Model model, HttpSession session) {

		// 세션 이메일 값 가져오기
		String SessionEmail = (String) session.getAttribute("email"); // 현재 로그인 중인 사람의 이메일
		model.addAttribute("email", SessionEmail);

		// 이메일로 회원 정보 가져오기
		MemberDTO memberDTO = memberService.memberInfo(SessionEmail);
		model.addAttribute("nickname", memberDTO.getNickname());

		model.addAttribute("maxSeqno", masterService.getMaxSeqno(SessionEmail) + 1);

	}

	// 요청사항 DB 업로드
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

	// 회원이 자신의 요청 목록 보기
	@GetMapping("/requestList")
	public void getMemberRequest(Model model, @RequestParam(name = "page", defaultValue = "1") int pageNum,
			@RequestParam(name = "keyword", defaultValue = "", required = false) String keyword, HttpSession session)
			throws Exception {

		int postNum = 10; // 한 화면에 보여지는 게시물 행의 갯수
		int pageListCount = 10; // 화면 하단에 보여지는 페이지리스트의 페이지 갯수

		String SessionEmail = (String) session.getAttribute("email"); // 현재 로그인 중인 사람의 이메일
		model.addAttribute("email", SessionEmail);

		// 이메일로 회원 정보 가져오기
		MemberDTO memberDTO = memberService.memberInfo(SessionEmail);
		model.addAttribute("nickname", memberDTO.getNickname());

		PageUtil page = new PageUtil();

		Page<RequestEntity> list = requestService.mylist(pageNum, postNum, keyword, SessionEmail);
		int totalCount = (int) list.getTotalElements();
		model.addAttribute("list", list);
		model.addAttribute("listIsEmpty", list.hasContent() ? "N" : "Y");
		model.addAttribute("totalElement", totalCount);
		model.addAttribute("postNum", postNum);
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pageList",
				page.getBoardRequestPageList(pageNum, postNum, pageListCount, totalCount, keyword));
	}

	// 회원이 자신의 요청사항 상세보기
	@GetMapping("/requestView")
	public void getRequestView(Model model, @RequestParam(name = "seqno", required = false) Long seqno,
			@RequestParam("page") int pageNum,
			@RequestParam(name = "keyword", defaultValue = "", required = false) String keyword, HttpSession session) {

		// 세션 이메일 값 가져오기
		String SessionEmail = (String) session.getAttribute("email"); // 현재 로그인 중인 사람의 이메일
		model.addAttribute("email", SessionEmail);

		// 이메일로 회원 정보 가져오기
		MemberDTO memberDTO = memberService.memberInfo(SessionEmail);
		model.addAttribute("nickname", memberDTO.getNickname());

		model.addAttribute("view", masterService.view(seqno));
		model.addAttribute("status", masterService.statusCheck(seqno));
		model.addAttribute("page", pageNum);
		model.addAttribute("keyword", keyword);
		model.addAttribute("pre_seqno", requestService.my_pre_seqno(seqno, SessionEmail, keyword));
		model.addAttribute("next_seqno", requestService.my_next_seqno(seqno, SessionEmail, keyword));
	}

	// 회원이 자신의 요청 게시물 삭제
	@Transactional
	@GetMapping("/requestDelete")
	public String getDelete(@RequestParam("seqno") Long seqno) throws Exception {

		// tbl_request에서 게시물을 삭제
		masterService.delete(seqno);
		return "redirect:/board/requestList"; // 삭제 후 목록 페이지로 리다이렉션
	}

	// 댓글 처리
	@ResponseBody
	@PostMapping("/requestReply")
	public List<Map<String, Object>> postReply(@RequestBody RequestReplyDTO dto, @RequestParam("kind") String kind)
			throws Exception {

		List<RequestReplyEntity> replyDataList;

		switch (kind) {
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
