package com.board.controller;

import java.util.ArrayList;
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

	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode rootNode = mapper.readTree(result);
	    JsonNode dailyList = rootNode.path("boxOfficeResult").path("dailyBoxOfficeList");

	    List<Map<String, Object>> list = new ArrayList<>();
	    for (JsonNode d : dailyList) {
	        Map<String, Object> map = mapper.convertValue(d, new TypeReference<Map<String, Object>>() {});
	        list.add(map);
	    }

	    // HTML 형식으로 데이터 변환
	    String htmlContent = convertToHtmlFormat(list);

	    // 모델에 데이터 추가
	    model.addAttribute("htmlContent", htmlContent);

	    // 뷰 이름 반환
	    return "daily-box-office";
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
