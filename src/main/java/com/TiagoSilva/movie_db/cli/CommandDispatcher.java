package com.TiagoSilva.movie_db.cli;

import com.TiagoSilva.movie_db.dto.MovieDto;
import com.TiagoSilva.movie_db.entity.Person;
import com.TiagoSilva.movie_db.exceptions.*;
import com.TiagoSilva.movie_db.initializer.ConsoleRunner;
import com.TiagoSilva.movie_db.service.ListingService;
import com.TiagoSilva.movie_db.service.MovieService;
import com.TiagoSilva.movie_db.service.PersonService;
import com.TiagoSilva.movie_db.ui.MoviePrinter;
import com.TiagoSilva.movie_db.ui.Prompts;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class CommandDispatcher {

    private final Tokenizer tokenizer = new Tokenizer();
    private final ListCommandParser listParser = new ListCommandParser();

    private final ListingService listingService;
    private final PersonService personService;
    private final MovieService movieService;
    private final Prompts prompts;
    private final MoviePrinter printer;

    public CommandDispatcher(ListingService listingService,
                             PersonService personService,
                             MovieService movieService,
                             Prompts prompts,
                             MoviePrinter printer) {
        this.listingService = listingService;
        this.personService = personService;
        this.movieService = movieService;
        this.prompts = prompts;
        this.printer = printer;
    }

    public boolean handle(String line, Scanner sc) {
        if (line.equalsIgnoreCase("exit") || line.equalsIgnoreCase("q") || line.equalsIgnoreCase("quit")) {
            System.out.println("Goodbye.");
            return false;
        }
        if (line.equalsIgnoreCase("help") || line.equalsIgnoreCase("h") || line.equalsIgnoreCase("menu")) {
            ConsoleRunner.printHelp();
            return true;
        }
        if (line.equalsIgnoreCase("back")) {
            System.out.println("- The command 'back' is reserved exclusively for canceling data entry within prompts.");
            return true;
        }

        TokenizationResult tr = tokenizer.tokenize(line);
        if (!tr.isOk()) {
            System.out.println("- Bad input format: " + tr.getError());
            return true;
        }

        List<Token> tokens = tr.getTokens();
        if (tokens.isEmpty()) return true;

        String cmd = tokens.get(0).getText();


        try {
            if (cmd.equals("l")) {
                MovieList query = listParser.parse(tokens);
                List<MovieDto> movies = listingService.listMovies(query);
                printer.printMovies(movies, query.isVerbose());
                return true;
            }

            if (cmd.equals("a")) {
                return handleAdd(tokens, sc);
            }

            if (cmd.equals("d")) {
                return handleDelete(tokens);
            }

            System.out.println("- Unknown command. Type 'help'.");
            return true;

        } catch (BackToMenuException ex) {
            System.out.println("- Returning to menu...");
            return true;
        } catch (BadCommandFormatExcpetion ex) {
            System.out.println("- Bad input format: " + ex.getMessage());
            return true;
        } catch (AlreadyExistsException | CannotDeleteDirectorException | NotFoundException ex) {
            System.out.println("- " + ex.getMessage());
            return true;
        } catch (IllegalArgumentException ex) {
            System.out.println("- " + ex.getMessage());
            return true;
        }
    }

    private boolean handleAdd(List<Token> tokens, Scanner sc) {
        if (tokens.size() < 2) {
            System.out.println("- Bad input format: missing -p or -m");
            return true;
        }
        String sw = tokens.get(1).getText();

        if (sw.equals("-p")) {
            String name = prompts.askNonEmpty(sc, "Name: ");
            String nat = prompts.askNonEmpty(sc, "Nationality: ");
            personService.addPerson(name, nat);
            System.out.println("OK.");
            return true;
        }

        if (sw.equals("-m")) {
            String title = prompts.askNonEmpty(sc, "Title: ");
            int lengthSeconds = prompts.askLengthSeconds(sc);
            String directorName = prompts.askExistingPersonName(sc, "Director: ");
            List<String> actorNames = prompts.askActors(sc);

            List<Person> actors = actorNames.stream()
                    .map(personService::getByExactName)
                    .toList();

            movieService.addOrMergeMovie(title, directorName, lengthSeconds, actors);
            System.out.println("OK.");
            return true;
        }

        System.out.println("- Bad input format: unknown switch for add");
        return true;
    }

    private boolean handleDelete(List<Token> tokens) {
        if (tokens.size() < 3) {
            System.out.println("- Bad input format: use d -p <exact name>");
            return true;
        }
        String sw = tokens.get(1).getText();
        if (!sw.equals("-p")) {
            System.out.println("- Bad input format: only d -p is supported");
            return true;
        }


        String name = tokens.get(2).getText();
        personService.deletePersonByName(name);
        System.out.println("OK.");
        return true;
    }
}