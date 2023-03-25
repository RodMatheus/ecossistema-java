package br.com.exemplo.comum.core.exception;

public class AcessoNegadoException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public AcessoNegadoException(String message) {
        super(message);
    }
}