package com.board.dto.movie;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActorDetailDTO {
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
    
    private String gender;
    
    @JsonProperty("also_known_as")
    private List<String> alsoKnownAs;
    
    @JsonProperty("known_for")
    private List<KnownFor> knownFor;
    
    @JsonProperty("movie_credits")
    private MovieCredits movieCredits;

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

    public static class MovieCredits {
        private List<Cast> cast;
        private List<Crew> crew;

        // Getters and setters
    }

    public static class Cast {
        private int id;
        private String title;
        private String character;
        
        @JsonProperty("release_date")
        private String releaseDate;
        
        // Getters and setters
    }

    public static class Crew {
        private int id;
        private String title;
        private String job;
        
        @JsonProperty("release_date")
        private String releaseDate;
        
        // Getters and setters
    }
}