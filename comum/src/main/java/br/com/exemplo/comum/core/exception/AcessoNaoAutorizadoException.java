package br.com.exemplo.comum.core.exception;

public class AcessoNaoAutorizadoException extends RuntimeException {

    public static final long serialVersionUID = 1L;

    public AcessoNaoAutorizadoException(String message) {
        super(message);
    }

    public AcessoNaoAutorizadoException() {
        super();
    }
}