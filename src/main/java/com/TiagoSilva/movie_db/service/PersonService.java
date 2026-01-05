package com.TiagoSilva.movie_db.service;

import com.TiagoSilva.movie_db.entity.Movie;
import com.TiagoSilva.movie_db.entity.Person;
import com.TiagoSilva.movie_db.exceptions.AlreadyExistsException;
import com.TiagoSilva.movie_db.exceptions.CannotDeleteDirectorException;
import com.TiagoSilva.movie_db.exceptions.NotFoundException;
import com.TiagoSilva.movie_db.repository.MovieRepository;
import com.TiagoSilva.movie_db.repository.PersonRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class PersonService {

    private final PersonRepository personRepository;
    private final MovieRepository movieRepository;

    public PersonService(PersonRepository personRepository, MovieRepository movieRepository) {
        this.personRepository = personRepository;
        this.movieRepository = movieRepository;
    }

    @Transactional
    public Person addPerson(String name, String nationality){
        if(name == null || name.isBlank()){
            throw new IllegalArgumentException("Name cannot be empty.");
        }
        if(nationality == null || nationality.isBlank()){
            throw new IllegalArgumentException("Nationality cannot be empty.");
        }
        if(personRepository.existsByName(name)){
            throw new AlreadyExistsException("Person with name " + name + "already exists.");
        }

        Person person = new Person();
        person.setName(name);
        person.setNationality(nationality);

        return personRepository.save(person);
    }

    @Transactional
    public Person getByExactName(String name){
        Person person = personRepository.findByName(name).orElse(null);
        if(person == null){
            throw new NotFoundException(name + "not found.");
        }
        return person;
    }

    @Transactional
    public void deletePersonByName(String name) {
        Person person = getByExactName(name);
        if(movieRepository.existsByDirector(person)){
            throw new CannotDeleteDirectorException("Cannot delete" + name + " because he is the director of at least one movie.");
        }

        List<Movie> movies = movieRepository.findDistinctByActorsContaining(person);
        for(Movie m : movies){
            Iterator<Person> it = m.getActors().iterator();
            while (it.hasNext()){
                Person a = it.next();

                if(a.getId() != null && person.getId() != null){
                    if(a.getId().equals(person.getId())) it.remove();
                }else if (a.getName() != null && a.getName().equals(person.getName())){
                    it.remove();
                }
            }
        }
        movieRepository.saveAll(movies);

        personRepository.delete(person);
    }

}
