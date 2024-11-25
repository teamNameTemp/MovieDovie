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
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity(name="weeklyTrend")
@Data
@Table(name="weeklyTrend")
@Builder
public class WeeklyTrendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator ="TREND_SEQ")
    @SequenceGenerator(name="TREND_SEQ",sequenceName="trend_seq",allocationSize=1,initialValue=1)
    private Long seqno;
    
    @Column(name="year")
    private int year;   
    @Column(name="month")
    private int month;
    @Column(name="month_of_week")
    private int monthOfWeek;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name="movieId")
    private MovieEntity movieId;
}
