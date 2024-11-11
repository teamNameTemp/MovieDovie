package com.board.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="memberLog")
@Table(name="tbl_member_log")
@IdClass(MemberLogEntityID.class)
public class MemberLogEntity {

	@JoinColumn(name="email")
	private String email;
	
	@Id
	private Long seqno;
	
	@Column(name="inouttime", nullable=false)
	private LocalDateTime inouttime;
	
	@Column(name="status", length=50, nullable=false)
	private String status;	
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@OnDelete(action = OnDeleteAction.CASCADE)
//	@JoinColumn(name="email", referencedColumnName = "email", insertable = false, updatable = false) // 관계 설정
//	private MemberEntity member; // MemberEntity와의 관계
}
