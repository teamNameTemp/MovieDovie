// package com.board.dto.movie;

// import java.util.List;

// import com.fasterxml.jackson.annotation.JsonProperty;

// import lombok.Data;

// @Data
// public class KobisMovieDTO {
	
// 	@JsonProperty("movieInfoResult")
// 	private MovieInfoResult movieInfoResult;

// 	@Data
// 	public static class MovieInfoResult {
// 		@JsonProperty("movieInfo")
// 		private MovieInfo movieInfo;
// 		@JsonProperty("source")
// 		private String source;
// 	}

// 	@Data
// 	public static class MovieInfo {
// 		@JsonProperty("movieCd")
// 		private String movieCd;
// 		@JsonProperty("movieNm")
// 		private String movieNm;
// 		@JsonProperty("movieNmEn")
// 		private String movieNmEn;
// 		@JsonProperty("movieNmOg")
// 		private String movieNmOg;
// 		@JsonProperty("showTm")
// 		private String showTm;
// 		@JsonProperty("prdtYear")
// 		private String prdtYear;
// 		@JsonProperty("openDt")
// 		private String openDt;
// 		@JsonProperty("prdtStatNm")
// 		private String prdtStatNm;
// 		@JsonProperty("typeNm")
// 		private String typeNm;
// 		@JsonProperty("overview")
// 		private String overview;
// 		@JsonProperty("posterPath")
// 		private String posterPath;

// 		@JsonProperty("nations")
// 		private List<Nation> nations;
// 		@JsonProperty("genres")
// 		private List<Genre> genres;
// 		@JsonProperty("directors")
// 		private List<Director> directors;
// 		@JsonProperty("actors")
// 		private List<Actor> actors;
// 		@JsonProperty("showTypes")
// 		private List<ShowType> showTypes;
// 		@JsonProperty("companys")
// 		private List<Company> companys;
// 		@JsonProperty("audits")
// 		private List<Audit> audits;
// 		@JsonProperty("staffs")
// 		private List<Staff> staffs;

// 		@Data
// 		public static class Nation {
// 			@JsonProperty("nationNm")
// 			private String nationNm;
// 		}

// 		@Data
// 		public static class Genre {
// 			@JsonProperty("genreNm")
// 			private String genreNm;
// 		}

// 		@Data
// 		public static class Director {
// 			@JsonProperty("peopleNm")
// 			private String peopleNm;
// 			@JsonProperty("peopleNmEn")
// 			private String peopleNmEn;
// 		}

// 		@Data
// 		public static class Actor {
// 			@JsonProperty("peopleNm")
// 			private String peopleNm;
// 			@JsonProperty("peopleNmEn")
// 			private String peopleNmEn;
// 			@JsonProperty("cast")
// 			private String cast;
// 			@JsonProperty("castEn")
// 			private String castEn;
// 		}

// 		@Data
// 		public static class ShowType {
// 			@JsonProperty("showTypeGroupNm")
// 			private String showTypeGroupNm;
// 			@JsonProperty("showTypeNm")
// 			private String showTypeNm;
// 		}

// 		@Data
// 		public static class Company {
// 			@JsonProperty("companyCd")
// 			private String companyCd;
// 			@JsonProperty("companyNm")
// 			private String companyNm;
// 			@JsonProperty("companyNmEn")
// 			private String companyNmEn;
// 			@JsonProperty("companyPartNm")
// 			private String companyPartNm;
// 		}

// 		@Data
// 		public static class Audit {
// 			@JsonProperty("auditNo")
// 			private String auditNo;
// 			@JsonProperty("watchGradeNm")
// 			private String watchGradeNm;
// 		}

// 		@Data
// 		public static class Staff {
// 			@JsonProperty("peopleNm")
// 			private String peopleNm;
// 			@JsonProperty("peopleNmEn")
// 			private String peopleNmEn;
// 			@JsonProperty("staffRoleNm")
// 			private String staffRoleNm;
// 		}
// 	}
// }