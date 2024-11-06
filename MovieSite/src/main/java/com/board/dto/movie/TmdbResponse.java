package com.board.dto.movie;

import java.util.List;

import lombok.Getter;

@Getter
public class TmdbResponse {
	@Getter
	public static class SearchResponse {
		private List<SearchResult> results;
	}

	@Getter
	public static class SearchResult {
		private int id;

		public Object getKnownForDepartment() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
