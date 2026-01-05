package com.TiagoSilva.movie_db.service;

import com.TiagoSilva.movie_db.entity.Movie;
import com.TiagoSilva.movie_db.entity.Person;
import com.TiagoSilva.movie_db.exceptions.NotFoundException;
import com.TiagoSilva.movie_db.repository.MovieRepository;
import com.TiagoSilva.movie_db.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final PersonRepository personRepository;
    private final PersonService personService;

    public MovieService(MovieRepository movieRepository, PersonRepository personRepository, PersonService personService){
        this.movieRepository = movieRepository;
        this.personRepository = personRepository;
        this.personService = personService;
    }

    @Transactional
    public Movie addOrMergeMovie(String title, String directorName, int lengthSeconds, List<Person> actors){
        if(title == null || title.isBlank()){
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        if(lengthSeconds <= 0){
            throw new IllegalArgumentException("Length must be greater than 0.");
        }

        Person director = personRepository.findByName(directorName).orElse(null);
        if(director == null){
            throw new NotFoundException(directorName + " not found.");
        }

        Movie movie = movieRepository.findByTitleAndDirector(title, director)
                .orElseGet(Movie::new);

        movie.setTitle(title);
        movie.setDirector(director);
        movie.setLengthSeconds(lengthSeconds);
        movie.getActors().addAll(actors);

        return movieRepository.save(movie);
    }

}
