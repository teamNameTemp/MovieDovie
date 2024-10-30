package com.board.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.board.service.KobisAPIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@RestController
@RequestMapping("/api/movies")
public class MovieController {
	private final KobisAPIService service;
	public MovieController(KobisAPIService service) {
		this.service = service;
	}
	
	@GetMapping("/daily")
	public String getDailyBoxOffice(@RequestParam("targetDt") String targetDate, Model model) throws JsonMappingException, JsonProcessingException {
	    String result = service.getDailyBoxOffice(targetDate);

	    System.out.println(result);
	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode rootNode = mapper.readTree(result);
	    JsonNode dailyList = rootNode.path("boxOfficeResult").path("dailyBoxOfficeList");

	    List<Map<String, Object>> list = new ArrayList<>();
	    for (JsonNode d : dailyList) {
	        Map<String, Object> map = mapper.convertValue(d, new TypeReference<Map<String, Object>>() {});
	        list.add(map);
	    }

	    for(int i = 0; i<list.size(); i++) {
	    	System.out.println(list.get(i));
	    }
	    
	    // HTML 형식으로 데이터 변환
	    String htmlContent = convertToHtmlFormat(list);

	    // 모델에 데이터 추가
	    model.addAttribute("htmlContent", htmlContent);

	    // 뷰 이름 반환
	    return "daily-box-office";
	}
	@GetMapping("/movie")
	public String getMovieDetail(@RequestParam("movieCd") String movieCd, Model model) throws JsonMappingException, JsonProcessingException {
		String result = service.getMovieInfo(movieCd);

		System.out.println(result);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(result);
		JsonNode movieInfo = rootNode.path("movieInfoResult").path("movieInfo");
		
		System.out.println("movieInfo");
		Map<String, Object> movieInfoMap = new HashMap<>();
		for (JsonNode d : movieInfo) {
			Map<String, Object> map = mapper.convertValue(d, new TypeReference<Map<String, Object>>() {
			});
			movieInfoMap = map;
		}

		for(Object info : movieInfo) {
			System.out.println(info);
		}
		  
		  // HTML 형식으로 데이터 변환 String htmlContent = convertToHtmlFormat(list);
		  
		  // 모델에 데이터 추가 model.addAttribute("htmlContent", htmlContent);
		 
//		List<Map<String, Object>> list = new ArrayList<>();
//		for (JsonNode d : movieInfo) {
//			Map<String, Object> map = mapper.convertValue(d, new TypeReference<Map<String, Object>>() {
//			});
//			list.add(map);
//		}
//
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i));
//		}
//		  
//		  // HTML 형식으로 데이터 변환 String htmlContent = convertToHtmlFormat(list);
//		  
//		  // 모델에 데이터 추가 model.addAttribute("htmlContent", htmlContent);
//		 

	    // 뷰 이름 반환
	    return "movieDetail";
	}
	
	@GetMapping("/actor")
	public String getActorInfo(@RequestParam("peopleCd") String peopleCd, Model model) throws JsonMappingException, JsonProcessingException {
		String result = service.getActorInfo(peopleCd);

		System.out.println(result);
		
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(result);
		JsonNode movieInfo = rootNode.path("movieInfoResult").path("movieInfo");
		
		System.out.println("movieInfo");
//		List<Map<String, Object>> list = new ArrayList<>();
//		for (JsonNode d : movieInfo) {
//			Map<String, Object> map = mapper.convertValue(d, new TypeReference<Map<String, Object>>() {
//			});
//			list.add(map);
//		}
//
//		for (int i = 0; i < list.size(); i++) {
//			System.out.println(list.get(i));
//		}
//		  
//		  // HTML 형식으로 데이터 변환 String htmlContent = convertToHtmlFormat(list);
//		  
//		  // 모델에 데이터 추가 model.addAttribute("htmlContent", htmlContent);
//		 

	    // 뷰 이름 반환
	    return "actorInfo";
	}

	@GetMapping("/weekly")
	public String getWeeklyBoxOffice(@RequestParam("targetDt") String targetDt, @RequestParam("weekGb") String weekGb) {
		String result = service.getWeeklyBoxOffice(targetDt,weekGb);
		
		System.out.println(result);
		
		return "weeklyBoxOffice";
	}
	// HTML 형식으로 변환하는 메서드
	private String convertToHtmlFormat(List<Map<String, Object>> data) {
	    StringBuilder htmlBuilder = new StringBuilder();
	    htmlBuilder.append("<table border='1'>");
	    htmlBuilder.append("<tr><th>Rank</th><th>Movie Name</th><th>Sales Amount</th></tr>");
	    
	    for (Map<String, Object> item : data) {
	        htmlBuilder.append("<tr>");
	        htmlBuilder.append("<td>").append(item.get("rnum")).append("</td>");
	        htmlBuilder.append("<td>").append(item.get("movieNm")).append("</td>");
	        htmlBuilder.append("<td>").append(item.get("salesAmt")).append("</td>");
	        htmlBuilder.append("</tr>");
	    }
	    
	    htmlBuilder.append("</table>");
	    return htmlBuilder.toString();
	}
}
