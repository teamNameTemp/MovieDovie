package com.board.entity.movie;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="crew")
@Table(name="crew")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrewEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long crewseqno;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@OnDelete(action=OnDeleteAction.CASCADE)
	@JoinColumn(name="seqno")
	private CreditEntity creditId;
	
	@Column(name="crewid")
	private Long crewId;
	
	@Column(name="crew_name")
	private String crewNm;
	
	private String jobTitle;
	
	private String department;
	
	private String profilePath;
	
}
