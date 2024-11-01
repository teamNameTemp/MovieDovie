package com.board.dto.movie;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActorDetailDTO {
	private int id;
	private String name;
	private String biography;
	private String birthday;
	private String placeOfBirth;
	private String profile_path;
}
