package br.com.exemplo.comum.infrastructure.util;

import br.com.exemplo.comum.core.exception.AplicacaoException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.StringUtils;

public abstract class Utilitarios {

    public static String UNACCENT_FUNCTION = "unaccent";
    private Utilitarios() {
        throw new AssertionError();
    }

    public static String likeFunction(final String valor) {
        return "%" + valor + "%";
    }

    public static String likeUnaccentFunction(final String valor) {
        return "%" + removeAccents(valor) + "%";
    }

    public static String removeAccents(final String valor) {
        return StringUtils.stripAccents(valor).toLowerCase();
    }

    public static String convertEntityLog(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());

            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new AplicacaoException("Ocorreu um erro ao tentar gerar log de auditoria.");
        }
    }
}