package br.com.exemplo.comum.core.exception;

public class ValidacaoException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public ValidacaoException(String message) {
        super(message);
    }
}