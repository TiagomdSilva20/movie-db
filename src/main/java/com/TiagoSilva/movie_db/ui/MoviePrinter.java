package com.TiagoSilva.movie_db.ui;

import com.TiagoSilva.movie_db.dto.MovieDto;
import com.TiagoSilva.movie_db.util.DurationFormat;
import org.springframework.stereotype.Component;


import java.util.List;

@Component
public class MoviePrinter {

    public void printMovies(List<MovieDto> movies, boolean verbose) {
        for (MovieDto m : movies) {
            String len = DurationFormat.formatFromSeconds(m.getLengthSeconds());
            System.out.println(m.getTitle() + " by " + m.getDirectorName() + ", " + len);

            if (verbose) {
                System.out.println("    Starring:");
                for (String a : m.getActors()) {
                    System.out.println("        - " + a);
                }
            }
        }
    }
}

