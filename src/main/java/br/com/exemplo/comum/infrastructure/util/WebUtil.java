package br.com.exemplo.comum.infrastructure.util;

public abstract class WebUtil {

    private WebUtil() {
        throw new AssertionError();
    }

    public static final String INTERNAL_SERVER_DEFAULT_RESPONSE = "Ocorreu um erro inesperado no sistema.";
    public static final String NOT_FOUND_DEFAULT_RESPONSE = "Recurso não encontrado.";
    public static final String FORBIDDEN_DEFAULT_RESPONSE = "Você não tem permissão para acessar este recurso.";
    public static final String UNAUTHORIZED_DEFAULT_RESPONSE = "Você não tem autorização para acessar a aplicação";
    public static final String BAD_REQUEST_DEFAULT_RESPONSE = "Lista de erros de validação";

    public static final String X_TOTAL_COUNT_HEADER = "X_TOTAL_COUNT";

    public static final String PAGE_DEFAULT = "1";
    public static final String SIZE_DEFAULT = "10";
}