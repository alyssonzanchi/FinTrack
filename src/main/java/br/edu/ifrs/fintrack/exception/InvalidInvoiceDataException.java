package br.edu.ifrs.fintrack.exception;

public class InvalidInvoiceDataException extends RuntimeException {
    public InvalidInvoiceDataException(String message) {
        super(message);
    }
}
