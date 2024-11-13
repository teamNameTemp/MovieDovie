package com.board.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.board.entity.movie.MovieEntity;

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
@Table(name = "tbl_reply")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "REPLY_SEQ")
    @SequenceGenerator(name = "REPLY_SEQ", sequenceName = "jpa_reply_seq", initialValue = 1, allocationSize = 1)
    private Long replyseqno;    // 댓글 고유번호
    
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="seqno", nullable=false)
    private MovieEntity seqno;  // 게시물(영화) 엔티티
    
    @Column(name = "movieseqno", nullable = false)
    private Long movieseqno;    // 영화 번호
    
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="email", nullable=false)
    private MemberEntity email; // 회원 엔티티
    
    @Column(name = "replywriter", length = 100, nullable = false)
    private String replywriter; // 댓글 작성자
    
    @Column(name = "replycontent", length = 1000, nullable = false)
    private String replycontent; // 댓글 내용
    
    @Column(name = "replyregdate", nullable = false)
    private LocalDateTime replyregdate; // 댓글 작성일
}