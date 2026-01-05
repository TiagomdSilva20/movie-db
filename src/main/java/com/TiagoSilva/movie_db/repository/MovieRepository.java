package com.TiagoSilva.movie_db.repository;

import com.TiagoSilva.movie_db.entity.Movie;
import com.TiagoSilva.movie_db.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MovieRepository extends JpaRepository<Movie, Long> {

    Optional<Movie> findByTitleAndDirector(String title, Person director);

    boolean existsByDirector(Person director);

    List<Movie> findDistinctByActorsContaining(Person actor);

    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.director")
    List<Movie> findAllWithDirector();

    @Query("SELECT DISTINCT m FROM Movie m LEFT JOIN FETCH m.director LEFT JOIN FETCH m.actors")
    List<Movie> findAllWithDirectorAndActors();

}
