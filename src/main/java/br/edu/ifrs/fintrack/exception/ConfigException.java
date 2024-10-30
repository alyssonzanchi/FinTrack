package br.edu.ifrs.fintrack.exception;

public class ConfigException extends FinTrackAppException {
    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
