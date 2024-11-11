package com.board.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_Rent")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CART_SEQ")
    @SequenceGenerator(name = "CART_SEQ", sequenceName = "jpa_cart_seq", initialValue = 1, allocationSize = 1)
    private Long cartId;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email")
    private MemberEntity member;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movieseqno")
    private MovieEntity movie;
    
    @Column(name = "regdate")
    private LocalDateTime regDate;

    public Object getCartSeqno() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCartSeqno'");
    }
}
