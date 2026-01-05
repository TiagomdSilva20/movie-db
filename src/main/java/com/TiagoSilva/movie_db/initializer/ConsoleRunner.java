package com.TiagoSilva.movie_db.initializer;

import com.TiagoSilva.movie_db.cli.CommandDispatcher;
import com.TiagoSilva.movie_db.repository.PersonRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@Order(2)
public class ConsoleRunner implements CommandLineRunner {

    private final PersonRepository personRepository;
    private final CommandDispatcher dispatcher;

    public ConsoleRunner(PersonRepository personRepository, CommandDispatcher dispatcher) {
        this.personRepository = personRepository;
        this.dispatcher = dispatcher;
    }

    @Override
    public void run(String... args) {
        System.out.println("Connecting to database...");
        try {
            personRepository.count();
            System.out.println("Connected to database.");
        } catch (Exception ex) {
            System.out.println("Failed to connect to database: " + ex.getMessage());
            System.exit(1);
            return;
        }

        printHelp();

        try (Scanner sc = new Scanner(System.in)) {
            while (true) {
                System.out.print("\n> ");
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                boolean shouldContinue = dispatcher.handle(line, sc);
                if (!shouldContinue) break;
            }
        }
    }

    public static void printHelp() {
        System.out.println("\nCommands:");
        System.out.println("  l [switches]                list movies");
        System.out.println("     -v                       verbose");
        System.out.println("     -t \"REGEX\"               filter title");
        System.out.println("     -d \"REGEX\"               filter director (quoted)");
        System.out.println("     -a \"REGEX\"               filter by actor (quoted)");
        System.out.println("     -la                      sort length asc");
        System.out.println("     -ld                      sort length desc");
        System.out.println("  a -p                        add person");
        System.out.println("  a -m                        add movie");
        System.out.println("  d -p <exact name>           delete person (cannot delete directors)");
        System.out.println("  help                        show menu");
        System.out.println("  back                        return to menu (when in prompts)");
        System.out.println("  exit                        quit application");
    }
}