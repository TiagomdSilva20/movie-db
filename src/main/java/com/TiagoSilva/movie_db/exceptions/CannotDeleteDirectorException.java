package com.TiagoSilva.movie_db.exceptions;

public class CannotDeleteDirectorException extends RuntimeException{

    public CannotDeleteDirectorException(String message) {
        super(message);
    }
}
