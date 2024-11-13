package br.edu.ifrs.fintrack.exception;

public class MissingCreditCardFieldException extends CreditCardCreationException {
    public MissingCreditCardFieldException(String fieldName) {
        super("Campo obrigatório ausente: " + fieldName);
    }
}
