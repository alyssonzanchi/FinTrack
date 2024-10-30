package br.edu.ifrs.fintrack.exception;

public class DatabaseIntegrityException extends RuntimeException {
    public DatabaseIntegrityException(String message) {
        super(message);
    }
}
