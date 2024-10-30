package com.board.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KobisAPIService {
	@Value("${kobis.api.key}")
	private String apiKey;
	
	private final RestTemplate restTemplate;
	public KobisAPIService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	public String getDailyBoxOffice(String targetDate) {
		 String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json" +
                 "?key=" + apiKey +
                 "&targetDt=" + targetDate;
    
		 return restTemplate.getForObject(url, String.class);
	}
	
	public String getMovieInfo(String movieCd) {
		 String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json" +
                "?key=" + apiKey +
                "&movieCd=" + movieCd;
   
		 return restTemplate.getForObject(url, String.class);
	}
	
	public String getActorInfo(String peopleCd) {
		 String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/people/searchPeopleInfo.json" +
               "?key=" + apiKey +
               "&peopleCd=" + peopleCd;
  
		 return restTemplate.getForObject(url, String.class);
	}
	
	public String getWeeklyBoxOffice(String targetDt,String weekGb) {
		String url = "http://www.kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchWeeklyBoxOfficeList.json"
				+ "?key=" + apiKey + 
				"&targetDt=" + targetDt +
				"&weekGb=" + weekGb;

		return restTemplate.getForObject(url, String.class);
	}

}
