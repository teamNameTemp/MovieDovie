package com.board.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.messaging.MessagingException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.board.dto.FavGenreDTO;
import com.board.dto.MailCheckDTO;
import com.board.dto.MemberDTO;
import com.board.service.FavGenreServiceImpl;
import com.board.service.MailService;
import com.board.service.MemberService;
import com.board.service.TmdbAPIServiceImpl;
import com.board.util.PasswordMaker;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@AllArgsConstructor
@Log4j2
@Controller
@RequestMapping("/member")
public class MemberController {

	private final MemberService service;
	private final BCryptPasswordEncoder pwdEncoder;
	private final TmdbAPIServiceImpl tmdb;
	private final FavGenreServiceImpl favGenre;
	private final MailService mailService;

		
	//로그인 화면 보기
	@GetMapping("/login")
	public void getLogin() { }
	
	//로그인 --> Spring Security에 의해 인터셉트
	@PostMapping("/login")
	public void postLogin() {}
	
	//로그인 처리 --> Spring Security에 의한 로그인 진행 후 처리 할 부분
	@ResponseBody
	@PostMapping("/loginCheck")
	public String postLoginCheck(MemberDTO member,HttpSession session) {
	
		//아이디 존재 여부 확인
		if(service.emailCheck(member.getEmail()) == 0) {
			return "{\"message\":\"EMAIL_NOT_FOUND\"}";
		}
		
		//패스워드가 올바르게 들어 왔는지 확인
		if(!pwdEncoder.matches(member.getPassword(), service.memberInfo(member.getEmail()).getPassword())) {
			//잘못된 패스워드 일때
			return "{\"message\":\"PASSWORD_NOT_FOUND\"}";
		}
			
		return "{\"message\":\"good\"}";
	}
	
	//로그아웃
	@GetMapping("/logout")
	public void getLogout(HttpSession session) {	
		String email = (String)session.getAttribute("email");
//		service.lastdateUpdate(email, "logout");
		service.memberLogRegistry(email, "logout");		
		session.invalidate(); 
		log.info("====================== 로그 아웃 ======================");
	}
	//로그아웃 전처리
	@PostMapping("/beforeLogout")
	public void getBeforeLogout(HttpSession session,Model model) {
		String email = (String)session.getAttribute("email");
		service.lastdateUpdate(email, "logout");
		service.memberLogRegistry(email, "logout");		
		session.invalidate(); 
		log.info("====================== 로그 아웃 전 로그 등록 및 세션 종료======================");
	}
	
	//회원 등록 화면 보기
	@GetMapping("/signup")
	public void getSignup() {	}
	
    // 회원 가입
	@ResponseBody
	@PostMapping("/signup")
	public Map<String,String> postSignup(MemberDTO member, @RequestParam("kind") String kind, 
			@RequestParam("fileUpload") MultipartFile mpr) throws Exception {
		
		//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 시작
		String os = System.getProperty("os.name").toLowerCase();
		String path;
		if(os.contains("win"))
			path = "c:\\Repository\\moviedovie\\profile\\";
		else 
			path = "/home/gladius/Repository/moviedovie/profile";
		
		//디렉토리가 존재하는지 체크해서 없다면 생성
		File p = new File(path);
		if(!p.exists()) p.mkdir();
		//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 종료
		
		File targetFile = null;
		String org_filename = "";
		String org_fileExtension = "";
		String stored_filename = "";
		
		if(!mpr.isEmpty()) {
			
			org_filename = mpr.getOriginalFilename();
			org_fileExtension = org_filename.substring(org_filename.lastIndexOf("."));			
			stored_filename = UUID.randomUUID().toString().replaceAll("-", "") + org_fileExtension;
			
			try {
				targetFile = new File(path + stored_filename);				
				mpr.transferTo(targetFile);
				
				member.setOrgFilename(stored_filename);
				member.setStoredFilename(stored_filename);
				member.setFilesize(mpr.getSize());				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		//회원 등록시
		if(kind.equals("I")) {
			log.info("회원가입성공!00");
			//패스워드 암호화
			member.setPassword(pwdEncoder.encode(member.getPassword()));
			//마지막 패스워드 변경일 --> 회원 가입 시 처음으로 패스워드 생성한 날도 포함
			//member.setLastpwdate(new Date());
			//회원 등록
			service.signup(member);	
		}
		
		//회원 수정시
		if(kind.equals("U")) {
			
			//프로필 이미지 변경 시에 기존 프로필 이미지 삭제 
			if(!mpr.isEmpty()) {
				MemberDTO m = service.memberInfo(member.getEmail());
				File file = new File(path + m.getStoredFilename());//수정되기 전의 stored_file
				file.delete();
			}		

    		String EncodedNewPW = pwdEncoder.encode(member.getPassword());
			member.setPassword(EncodedNewPW);
			
			//회원 수정
			service.modifyMemberInfo(member);
		}		
		
		Map<String,String> data = new HashMap<>();
		data.put("message", "good");
		data.put("username", URLEncoder.encode(member.getUsername(),"UTF-8"));
		
		return data;

	}
	
	@GetMapping("/modifyMemberInfoOauth")
	public void getMemberInfoModifyOauth(Model model, HttpSession session) {	
		String email = (String)session.getAttribute("email");
		model.addAttribute("member", service.memberInfo(email));		
	}
	
	
	//이메일 중복 확인
    @ResponseBody
    @PostMapping("/emailCheck")
    public Map<String, Boolean> postEmailCheck(@RequestBody Map<String, String> request) throws Exception {
        String email = request.get("email");
        int result = service.emailCheck(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", result > 0); // result가 0보다 크면 이미 존재하는 이메일임
        return response;
    }
	
    // 닉네임 중복 확인
    @ResponseBody
    @PostMapping("/nicknameCheck")
    public Map<String, Boolean> postNicknameCheck(@RequestBody Map<String, String> request) throws Exception {
        String nickname = request.get("nickname");
        int result = service.nicknameCheck(nickname);
        Map<String, Boolean> response = new HashMap<>();
        response.put("exists", result > 0); // result가 0보다 크면 이미 존재하는 닉네임임
        return response;
    }

	
	//회원 정보 수정 화면 보기
	@GetMapping("/modifyMemberInfo")
	public void getMemberInfoModify(Model model, HttpSession session) {	
		String email = (String)session.getAttribute("email");
		model.addAttribute("member", service.memberInfo(email));		
		//연결
	}
	
	
	//회원 프로필 이미지 보기
	@GetMapping("viewProfile/{email}")
	public void filedownload(@PathVariable String email, HttpServletResponse rs) throws Exception {		
		
		//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 시작
		String os = System.getProperty("os.name").toLowerCase();
		String path;
		if(os.contains("win"))
			path = "c:\\Repository\\profile\\";
		else 
			path = "/home/tjdwn/Repository/profile";
		
		//디렉토리가 존재하는지 체크해서 없다면 생성
		File p = new File(path);
		if(!p.exists()) p.mkdir();
		//운영체제에 따라 이미지가 저장될 디렉토리 구조 설정 종료

		MemberDTO member = service.memberInfo(email);
		
		//다운로드할 파일의 경로와 파일명을 매개변수로 입력 받아 byte 데이터타입의 1차원 배열로 저장
		File file = new File(path + member.getStoredFilename());
		if (!file.exists()) {
		    throw new FileNotFoundException("File not found at: " + file.getAbsolutePath());
		}

		// 파일을 byte[]로 읽기
		byte[] fileBytes = Files.readAllBytes(file.toPath());

		
		//예) HTTP Response Header는 Content-Disposition: attachment; filename="hello.jpg";
		//   HTTP Response Body에는 1차원 바이트 타입으로 변환된 배열을 넣음
		rs.setContentType("application/octet-stream");
		rs.setContentLength(fileBytes.length);
		rs.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(member.getOrgFilename(), "UTF-8") + "\";");
		rs.getOutputStream().write(fileBytes); //stream을 통해 1차원 byte 타입 배열로 변환된 데이터(추후 파일로 변환)를 Buffer에 씀...
		rs.getOutputStream().flush(); //버퍼에 있는 내용을 write
		rs.getOutputStream().close(); //스트림 닫기
	}
	
	//회원가입 이메일 인증
    @ResponseBody
    @PostMapping("/signup/mailConfirm")		   
    public Map<String,Object> mailConfirm(MailCheckDTO mailCheckDTO) throws MessagingException, UnsupportedEncodingException, jakarta.mail.MessagingException  {
        Map<String,Object> data = new HashMap<>();
    	
    	String authCode = mailService.sendEmail(mailCheckDTO.getEmail());
        System.out.println("이메일 인증");
        data.put("ok", true);
        data.put("message", "good");
        data.put("code", authCode); // 인증번호를 클라이언트로 전달
        System.out.println("코드는" + authCode);
        return 	data;
    }
	
	// 추천 장르
	@GetMapping("/selectGenre")
	public void getMovieSelect(Model model, HttpSession session) {
		Map<String,Integer> map = tmdb.getGenreList().block();
		
		model.addAttribute("genres",map);
		
		String nickname = (String)session.getAttribute("nickname");
		model.addAttribute("nickname", nickname);
		
		Optional<Map<String, Object>> genre = favGenre.getFavGenre();
		model.addAttribute("favGenre", genre.orElse(null));
	}
	
	@ResponseBody
	@PostMapping("/selectGenre")
	public Map<String,Boolean> postSelectGenres(@RequestBody Map<String, List<String>> payload, HttpSession session){
		List<String> selectedGenres = payload.get("genres");
		FavGenreDTO favGenreDTO = new FavGenreDTO();
		switch(selectedGenres.size()) {
		case 1:
			favGenreDTO.setGenre1(selectedGenres.get(0));
			break;
		case 2:
			favGenreDTO.setGenre1(selectedGenres.get(0));
			favGenreDTO.setGenre2(selectedGenres.get(1));
			break;
		case 3:
			favGenreDTO.setGenre1(selectedGenres.get(0));
			favGenreDTO.setGenre2(selectedGenres.get(1));
			favGenreDTO.setGenre3(selectedGenres.get(2));
			break;
		}
		
		String email = (String)session.getAttribute("email");
		favGenreDTO.setEmail(email);

		favGenre.saveTheFavGenre(favGenreDTO);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("success", true);
		return response;
	}
		
	
	//이전 비밀번호와 비교
    @ResponseBody
    @PostMapping("/checkPreviousPassword")
    public Map<String, Boolean> checkPreviousPassword(@RequestBody Map<String, String> request, HttpSession session) {
        Map<String, Boolean> response = new HashMap<>();
        String newPassword = request.get("password");

        // 세션에서 현재 사용자 이메일 가져오기
        String email = (String) session.getAttribute("email");

        if (email == null) {
            response.put("isSame", false);
            return response;
        }

        // 사용자 정보 가져오기
        MemberDTO member = service.memberInfo(email);

        // 이전 비밀번호와 비교
        boolean isSame = pwdEncoder.matches(newPassword, member.getPassword());
        response.put("isSame", isSame);
        return response;
    }


	//이메일 찾기 화면 보기
	@GetMapping("/searchEmail")
	public void getSearchEmail() {}
	
	//이메일 찾기
	@ResponseBody
	@PostMapping("/searchEmail")
	public String postSearchEmail(MemberDTO member) {
		
		String email = service.searchEmail(member) == null?"ID_NOT_FOUND":service.searchEmail(member);		
		return "{\"message\":\"" + email + "\"}";
	}
	
	//비밀번호 찾기
	@GetMapping("/searchPassword")
	public void getSearchPassword(){
	}
	
	//신규 비밀번호
	@GetMapping("/modifyMemberPassword")
	public void getModifyMemberPassword() {
		
	}
	
	//비밀번호 찾기
	@ResponseBody
	@PostMapping("/searchPassword/mailConfirm")
	public Map<String, Object> postSearchPassword(@RequestParam("email") String email) throws MessagingException, UnsupportedEncodingException {
	    Map<String, Object> data = new HashMap<>();

	    try {
	        // 인증번호 생성 및 이메일 전송
	        String authCode = mailService.sendEmail(email);
	        System.out.println("이메일 인증: " + authCode);

	        data.put("ok", true);
	        data.put("message", "good");
	        data.put("code", authCode); // 인증번호를 클라이언트로 전달
	    } catch (Exception e) {
	        System.err.println("이메일 인증 실패: " + e.getMessage());
	        data.put("ok", false);
	        data.put("message", "fail");
	    }

	    return data;
	}

	//비밀번호 찾기 인증
    @ResponseBody
    @PostMapping("/searchPassword/tempPW")		   
    public Map<String,Object> tempPW(@RequestParam("email") String email, @RequestParam("username") String username) throws MessagingException, UnsupportedEncodingException  {
       
    	Map<String,Object> data = new HashMap<>();
    
    	if(service.memberInfo(email).getUsername().equals(username)) {
    		data.put("message", "good");
    		

        	//임시 패스워드 생성	
    		PasswordMaker pMaker = new PasswordMaker();
    		String rawTempPW = pMaker.tempPasswordMaker();
    		service.makeTempPW(email, pwdEncoder.encode(rawTempPW));

            data.put("rawTempPW", rawTempPW);
            
    	}
        //아이디 존재 여부 확인
    	else if(service.emailCheck(email) == 0) {
    		data.put("message", "EMAIL_NOT_FOUND");
    	}
    	//username 확인
    	else if(!service.memberInfo(email).getUsername().equals(username)) {
    		data.put("message", "USERNAME_NOT_FOUND");
    	}
    	

        return data;
    }
    
    //신규 비밀번호 변경
    @ResponseBody
    @PostMapping("/modifyMemberPassword")		   
    public Map<String,Object> postModifyMemberPassword(@RequestParam("email") String email, @RequestParam("old_password") String old_password, @RequestParam("new_password") String new_password) throws MessagingException, UnsupportedEncodingException  {
       
    	Map<String,Object> data = new HashMap<>();
    
    	if (pwdEncoder.matches(old_password, service.memberInfo(email).getPassword())) {
    	    // 기존 패스워드와 입력한 old_password 비교 성공
    	    service.makeTempPW(email, pwdEncoder.encode(new_password));

            data.put("message", "good");
            
    	}
        //아이디 존재 여부 확인
    	else if(service.emailCheck(email) == 0) {
    		data.put("message", "EMAIL_NOT_FOUND");
    	}
    	//기존 패스워드 확인
    	else {
    		data.put("message", "PASSWORD_NOT_FOUND");
    	}
    	
        return data;
    }
	
	//카카오
	 @GetMapping("/auth/kakao")
	 public String kakaoLogin() {
	    return "redirect:/member/selectGenre"; 
	 }
	 
//	@GetMapping("/member/auth/kakao/callback")
//	public String kakaoCallback() {
//		return "redirect:/board/selectGenre";
//	}
	

}
