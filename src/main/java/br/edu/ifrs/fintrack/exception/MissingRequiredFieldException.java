package br.edu.ifrs.fintrack.exception;

public class MissingRequiredFieldException extends AccountCreationException {
    public MissingRequiredFieldException(String fieldName) {
        super("Campo obrigatório ausente: " + fieldName);
    }
}
