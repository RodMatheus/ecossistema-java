package br.com.exemplo.comum.core.exception;

public class AplicacaoException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public AplicacaoException(String message) {
        super(message);
    }
}