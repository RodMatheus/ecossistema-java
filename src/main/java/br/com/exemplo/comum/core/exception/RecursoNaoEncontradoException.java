package br.com.exemplo.comum.core.exception;

public class RecursoNaoEncontradoException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public RecursoNaoEncontradoException(String message) {
        super(message);
    }
}