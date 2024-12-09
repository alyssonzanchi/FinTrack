package br.edu.ifrs.fintrack.exception;

public class MissingInvoiceFieldException extends RuntimeException {
    public MissingInvoiceFieldException(String fieldName) {
        super("O campo '" + fieldName + "' é obrigatório.");
    }
}
