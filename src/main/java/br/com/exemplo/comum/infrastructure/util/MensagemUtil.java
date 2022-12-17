package br.com.exemplo.comum.infrastructure.util;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class MensagemUtil {

    private MessageSource messageSource;

    public MensagemUtil(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String mensagemPersonalizada(final String codigo) {
        return this.messageSource.getMessage(codigo, null, Locale.getDefault());
    }
}