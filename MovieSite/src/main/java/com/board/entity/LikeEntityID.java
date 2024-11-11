package com.board.entity;

import java.io.Serializable;

import lombok.EqualsAndHashCode;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode //원본과 전송된 것과의 차이를 비교
public class LikeEntityID implements Serializable{ //변수값들을 serialize화 하기 위해서 필요한 작업을 해 줌

	private static final long serialVersionUID = 1L;
	private Long seqno;
	private String email;	
}
