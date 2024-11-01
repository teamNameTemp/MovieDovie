package com.board.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name="daily")
@Table(name = "tbl_daily")
public class DailyBoxOfficeEntity {

    @Id
    @Column(name = "today")
    private String date;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "daily_box_office_date")
    private List<DailyMovieEntity> movies = new ArrayList<>();

    public void addMovie(DailyMovieEntity movie) {
        movies.add(movie);
    }
}