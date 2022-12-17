package br.com.exemplo.comum.core.message;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

@Configuration
public class MessageConfig {

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();

        messageSource.setBasenames("classpath:messages");
        messageSource.setUseCodeAsDefaultMessage(Boolean.FALSE);
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(new Locale("pt", "BR"));

        return messageSource;
    }
}