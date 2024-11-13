package br.edu.ifrs.fintrack.exception;

public class AccountCreationException extends RuntimeException {
    public AccountCreationException(String message) {
        super(message);
    }
}
