package com.TiagoSilva.movie_db.dto;

import java.util.ArrayList;
import java.util.List;

public class MovieDto {
    private String title;
    private String directorName;
    private int lengthSeconds;
    private List<String> actors = new ArrayList<>();

    public MovieDto(String title, String directorName, int lengthSeconds, List<String> actors) {
        this.title = title;
        this.directorName = directorName;
        this.lengthSeconds = lengthSeconds;
        this.actors = actors;
    }

    public String getTitle() {
        return title;
    }

    public String getDirectorName() {
        return directorName;
    }

    public int getLengthSeconds() {
        return lengthSeconds;
    }

    public List<String> getActors() {
        return actors;
    }
}
