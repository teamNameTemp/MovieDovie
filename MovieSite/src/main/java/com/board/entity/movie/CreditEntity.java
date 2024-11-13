package com.board.entity.movie;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;

@Entity(name = "credit")
@Table(name = "credit")
@Data
public class CreditEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CREDIT_SEQ")
	@SequenceGenerator(name = "CREDIT_SEQ", sequenceName = "credit_seq", allocationSize = 1,initialValue = 1)
	private Long seqno;

	@OneToOne
	@JoinColumn(name = "movieId")
	private MovieEntity movieId;
	

}
