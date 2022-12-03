package br.com.exemplo.comum.config.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

public @interface CheckSecurity {

    @interface comum {

        @PreAuthorize("hasAuthority('COMUM_ADMIN')")
        @Retention(RUNTIME)
        @Target(METHOD)
        @interface admin{}

        @PreAuthorize("hasAuthority('COMUM_EDIT')")
        @Retention(RUNTIME)
        @Target(METHOD)
        @interface edit{}

        @PreAuthorize("hasAuthority('COMUM_VIEW')")
        @Retention(RUNTIME)
        @Target(METHOD)
        @interface view{}
    }
}
