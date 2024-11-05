package com.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity(name="member")
@Table(name="tbl_member")
public class MemberEntity {
    
    @Id
    @Column(name="email", length=320, nullable=false)
    private String email;
    
    @Column(name="username", length=50, nullable=false)
    private String username;
    
    @Column(name="password", length=100, nullable=false)
    private String password;

    
    // 필요한 경우 다른 엔티티와의 관계 정의
    // @OneToMany(mappedBy = "member")
    // private List<LikeEntity> likes;
}