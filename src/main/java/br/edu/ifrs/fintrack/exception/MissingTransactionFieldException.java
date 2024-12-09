package br.edu.ifrs.fintrack.exception;

public class MissingTransactionFieldException extends RuntimeException {
    public MissingTransactionFieldException(String fieldName) {
        super("Campo obrigatório ausente: " + fieldName);
    }
}