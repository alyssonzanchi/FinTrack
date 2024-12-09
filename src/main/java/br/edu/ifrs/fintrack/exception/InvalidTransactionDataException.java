package br.edu.ifrs.fintrack.exception;

public class InvalidTransactionDataException extends RuntimeException {
    public InvalidTransactionDataException(String message) {
        super(message);
    }
}
