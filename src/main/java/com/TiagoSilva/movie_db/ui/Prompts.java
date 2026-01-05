package com.TiagoSilva.movie_db.ui;

import com.TiagoSilva.movie_db.exceptions.BackToMenuException;
import com.TiagoSilva.movie_db.exceptions.NotFoundException;
import com.TiagoSilva.movie_db.service.PersonService;
import com.TiagoSilva.movie_db.util.DurationFormat;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class Prompts {

    private final PersonService personService;

    public Prompts(PersonService personService) {
        this.personService = personService;
    }

    public String askNonEmpty(Scanner sc, String label) {
        while (true) {
            System.out.print(label);
            String s = sc.nextLine().trim();
            if (s.equalsIgnoreCase("back")) {
                throw new BackToMenuException("User requested to go back to menu");
            }
            if (!s.isBlank()) return s;
        }
    }

    public int askLengthSeconds(Scanner sc) {
        while (true) {
            System.out.print("Length: ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                throw new BackToMenuException("User requested to go back to menu");
            }
            try {
                return DurationFormat.parseToSeconds(input);
            } catch (IllegalArgumentException ex) {
                System.out.println("- Bad input format (hh:mm:ss), try again!");
            }
        }
    }

    public String askExistingPersonName(Scanner sc, String label) {
        while (true) {
            System.out.print(label);
            String name = sc.nextLine().trim();
            if (name.equalsIgnoreCase("back")) {
                throw new BackToMenuException("User requested to go back to menu");
            }
            try {
                personService.getByExactName(name);
                return name;
            } catch (NotFoundException ex) {
                System.out.println("- We could not find \"" + name + "\", try again!");
            }
        }
    }

    public List<String> askActors(Scanner sc) {
        List<String> names = new ArrayList<>();

        System.out.print("Starring: ");
        while (true) {
            String name = sc.nextLine().trim();
            if (name.equalsIgnoreCase("back")) {
                throw new BackToMenuException("User requested to go back to menu");
            }
            if (name.equalsIgnoreCase("exit")) {
                return names;
            }
            if (name.isBlank()) {
                System.out.print("");
                continue;
            }
            try {
                personService.getByExactName(name);
                names.add(name);

            } catch (NotFoundException ex) {
                System.out.println("- We could not find \"" + name + "\", try again!");
            }
        }
    }
}