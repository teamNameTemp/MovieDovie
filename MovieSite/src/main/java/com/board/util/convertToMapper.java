package com.board.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class convertToMapper {
	public static List<Map<String,Object>> getDailyBoxOffice(String result) throws JsonMappingException, JsonProcessingException{
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readTree(result);
		JsonNode dailyList = rootNode.path("boxOfficeResult").path("dailyBoxOfficeList");

		List<Map<String, Object>> list = new ArrayList<>();
		for (JsonNode d : dailyList) {
			Map<String, Object> map = mapper.convertValue(d, new TypeReference<Map<String, Object>>() {
			});
			list.add(map);
		}
		return list;
	}
	
	public static Map<String, Object> getMovieInfo(String result) throws JsonMappingException, JsonProcessingException {
	    ObjectMapper mapper = new ObjectMapper();
	    JsonNode rootNode = mapper.readTree(result);
	    JsonNode movieInfo = rootNode.path("movieInfoResult").path("movieInfo");

	    Map<String, Object> movieInfoMap = new HashMap<>();
	    movieInfoMap.put("movieCd", movieInfo.get("movieCd").asText());
	    movieInfoMap.put("movieNm", movieInfo.get("movieNm").asText());
	    movieInfoMap.put("movieNmEn", movieInfo.get("movieNmEn").asText());
	    movieInfoMap.put("movieNmOg", movieInfo.get("movieNmOg").asText());
	    movieInfoMap.put("showTm", movieInfo.get("showTm").asText());
	    movieInfoMap.put("prdtYear", movieInfo.get("prdtYear").asText());
	    movieInfoMap.put("openDt", movieInfo.get("openDt").asText());
	    movieInfoMap.put("prdtStatNm", movieInfo.get("prdtStatNm").asText());
	    movieInfoMap.put("typeNm", movieInfo.get("typeNm").asText());

	    // nations 처리
	    List<String> nations = new ArrayList<>();
	    for (JsonNode nation : movieInfo.path("nations")) {
	        nations.add(nation.get("nationNm").asText());
	    }
	    movieInfoMap.put("nations", nations);

	    // genres 처리
	    List<String> genres = new ArrayList<>();
	    for (JsonNode genre : movieInfo.path("genres")) {
	        genres.add(genre.get("genreNm").asText());
	    }
	    movieInfoMap.put("genres", genres);

	    // directors 처리
	    List<Map<String, String>> directors = new ArrayList<>();
	    for (JsonNode director : movieInfo.path("directors")) {
	        Map<String, String> directorMap = new HashMap<>();
	        directorMap.put("peopleNm", director.get("peopleNm").asText());
	        directorMap.put("peopleNmEn", director.get("peopleNmEn").asText());
	        directors.add(directorMap);
	    }
	    movieInfoMap.put("directors", directors);

	    // actors 처리
	    List<Map<String, String>> actors = new ArrayList<>();
	    for (JsonNode actor : movieInfo.path("actors")) {
	        Map<String, String> actorMap = new HashMap<>();
	        actorMap.put("peopleNm", actor.get("peopleNm").asText());
	        actorMap.put("peopleNmEn", actor.get("peopleNmEn").asText());
	        actorMap.put("cast", actor.get("cast").asText());
	        actorMap.put("castEn", actor.get("castEn").asText());
	        actors.add(actorMap);
	    }
	    movieInfoMap.put("actors", actors);

	    // showTypes 처리
	    List<Map<String, String>> showTypes = new ArrayList<>();
	    for (JsonNode showType : movieInfo.path("showTypes")) {
	        Map<String, String> showTypeMap = new HashMap<>();
	        showTypeMap.put("showTypeGroupNm", showType.get("showTypeGroupNm").asText());
	        showTypeMap.put("showTypeNm", showType.get("showTypeNm").asText());
	        showTypes.add(showTypeMap);
	    }
	    movieInfoMap.put("showTypes", showTypes);

	    // companys 처리
	    List<Map<String, String>> companys = new ArrayList<>();
	    for (JsonNode company : movieInfo.path("companys")) {
	        Map<String, String> companyMap = new HashMap<>();
	        companyMap.put("companyCd", company.get("companyCd").asText());
	        companyMap.put("companyNm", company.get("companyNm").asText());
	        companyMap.put("companyNmEn", company.get("companyNmEn").asText());
	        companyMap.put("companyPartNm", company.get("companyPartNm").asText());
	        companys.add(companyMap);
	    }
	    movieInfoMap.put("companys", companys);

	    // audits 처리
	    List<Map<String, String>> audits = new ArrayList<>();
	    for (JsonNode audit : movieInfo.path("audits")) {
	        Map<String, String> auditMap = new HashMap<>();
	        auditMap.put("auditNo", audit.get("auditNo").asText());
	        auditMap.put("watchGradeNm", audit.get("watchGradeNm").asText());
	        audits.add(auditMap);
	    }
	    movieInfoMap.put("audits", audits);

	    // staffs 처리 (빈 배열이지만 일관성을 위해 포함)
	    movieInfoMap.put("staffs", new ArrayList<>());

	    return movieInfoMap;
	}
}
