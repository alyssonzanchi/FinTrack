package br.edu.ifrs.fintrack.exception;

public class InvalidCreditCardDataException extends CreditCardCreationException {
    public InvalidCreditCardDataException(String message) {
        super("Dados inválidos para criação do cartão de crédito: " + message);
    }
}
