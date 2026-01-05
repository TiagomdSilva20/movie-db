package com.TiagoSilva.movie_db.service;

import com.TiagoSilva.movie_db.cli.MovieList;
import com.TiagoSilva.movie_db.dto.MovieDto;
import com.TiagoSilva.movie_db.entity.Movie;
import com.TiagoSilva.movie_db.entity.Person;
import com.TiagoSilva.movie_db.repository.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class ListingService {

    private final MovieRepository movieRepository;

    public ListingService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public List<MovieDto> listMovies(MovieList query) {
        boolean needsActors = query.isVerbose() || query.getActorPattern() != null;

        List<Movie> all = needsActors
                ? movieRepository.findAllWithDirectorAndActors()
                : movieRepository.findAllWithDirector();


        List<Movie> filtered = new ArrayList<>();
        for (Movie m : all) {
            if (!matches(query.getTitlePattern(), safe(m.getTitle()))) continue;
            if (!matches(query.getDirectorPattern(), safeDirectorName(m))) continue;
            if (!matchesActor(query.getActorPattern(), m)) continue;
            filtered.add(m);
        }


        sort(filtered, query.getSortOrder());


        List<MovieDto> out = new ArrayList<>();
        for (Movie m : filtered) {
            String directorName = safeDirectorName(m);

            List<String> actorNames = new ArrayList<>();
            if (query.isVerbose()) {
                for (Person a : m.getActors()) {
                    actorNames.add(safe(a.getName()));
                }
                actorNames.sort(Comparator.comparing(String::toLowerCase));
            }

            out.add(new MovieDto(m.getTitle(), directorName, m.getLengthSeconds(), actorNames));
        }

        return out;
    }

    private boolean matches(Pattern p, String text) {
        if (p == null) return true;
        return p.matcher(text).find();
    }

    private boolean matchesActor(Pattern p, Movie m) {
        if (p == null) return true;
        for (Person a : m.getActors()) {
            if (p.matcher(safe(a.getName())).find()) return true;
        }
        return false;
    }

    private String safe(String s) { return s == null ? "" : s; }

    private String safeDirectorName(Movie m) {
        if (m.getDirector() == null) return "";
        return safe(m.getDirector().getName());
    }

    private void sort(List<Movie> movies, MovieList.SortOrder order) {
        Comparator<Movie> cmp;

        if (order == MovieList.SortOrder.LENGTH_ASC) {
            cmp = (a, b) -> {
                int c = Integer.compare(a.getLengthSeconds(), b.getLengthSeconds());
                if (c != 0) return c;
                return safe(a.getTitle()).compareToIgnoreCase(safe(b.getTitle()));
            };
        } else if (order == MovieList.SortOrder.LENGTH_DESC) {
            cmp = (a, b) -> {
                int c = Integer.compare(b.getLengthSeconds(), a.getLengthSeconds());
                if (c != 0) return c;
                return safe(a.getTitle()).compareToIgnoreCase(safe(b.getTitle()));
            };
        } else {
            cmp = (a, b) -> safe(a.getTitle()).compareToIgnoreCase(safe(b.getTitle()));
        }

        movies.sort(cmp);
    }
}
