package br.edu.ifrs.fintrack.exception;

public class DataAccessException extends RuntimeException {
    public DataAccessException(String message) {
        super(message);
    }
}
