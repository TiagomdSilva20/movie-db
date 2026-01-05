package com.TiagoSilva.movie_db.initializer;

import com.TiagoSilva.movie_db.entity.Person;
import com.TiagoSilva.movie_db.repository.MovieRepository;
import com.TiagoSilva.movie_db.repository.PersonRepository;
import com.TiagoSilva.movie_db.service.MovieService;
import com.TiagoSilva.movie_db.service.PersonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
public class DataInit implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final MovieRepository movieRepository;
    private final PersonService personService;
    private final MovieService movieService;

    public DataInit(PersonRepository personRepository,
                    MovieRepository movieRepository,
                    PersonService personService,
                    MovieService movieService) {
        this.personRepository = personRepository;
        this.movieRepository = movieRepository;
        this.personService = personService;
        this.movieService = movieService;
    }

    @Override
    public void run(String... args) {
        if (personRepository.count() > 0 || movieRepository.count() > 0) {
            return;
        }

        loadDataFromFile();
    }

    private void loadDataFromFile() {
        try {

            ClassPathResource resource = new ClassPathResource("data.txt");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));

            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                if (line.startsWith("PERSON,")) {
                    String data = line.substring(7);
                    String[] parts = data.split(",", 2);

                    String name = parts[0].trim();
                    String nationality = parts[1].trim();
                    personService.addPerson(name, nationality);
                }

                else if (line.startsWith("MOVIE,")) {
                    String data = line.substring(6);
                    String[] parts = data.split(",", 4);

                    String title = parts[0].trim();
                    String director = parts[1].trim();
                    int length = Integer.parseInt(parts[2].trim());

                    String[] actorNames = parts[3].split(";");
                    List<Person> actors = Arrays.stream(actorNames)
                            .map(String::trim)
                            .map(personService::getByExactName)
                            .toList();

                    movieService.addOrMergeMovie(title, director, length, actors);
                }
            }

            reader.close();

        } catch (IOException e) {
            throw new RuntimeException("Failed to load data from file", e);
        }
    }
}
