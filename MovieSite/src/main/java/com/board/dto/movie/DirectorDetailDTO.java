package com.board.dto.movie;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DirectorDetailDTO {
    private int id;
    private String name;
    
    @JsonProperty("known_for_department")
    private String knownForDepartment;
    
    private String biography;
    private String birthday;
    private String deathday;
    
    @JsonProperty("place_of_birth")
    private String placeOfBirth;
    
    @JsonProperty("profile_path")
    private String profilePath;
    
    private double popularity;
    
    @JsonProperty("known_for")
    private List<KnownFor> knownFor;

    // Getters and setters

    public static class KnownFor {
        private int id;
        private String title;
        
        @JsonProperty("media_type")
        private String mediaType;
        
        @JsonProperty("release_date")
        private String releaseDate;
        
        // Getters and setters
    }
}