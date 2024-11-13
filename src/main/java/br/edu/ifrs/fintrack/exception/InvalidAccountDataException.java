package br.edu.ifrs.fintrack.exception;

public class InvalidAccountDataException extends AccountCreationException {
    public InvalidAccountDataException(String message) {
        super("Dados inválidos para a criação da conta: " + message);
    }
}
