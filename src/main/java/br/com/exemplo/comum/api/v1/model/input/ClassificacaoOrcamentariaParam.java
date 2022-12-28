package br.com.exemplo.comum.api.v1.model.input;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClassificacaoOrcamentariaParam(
        @NotBlank
        String nome,

        @NotNull
        Boolean despesa,

        Long pai) {
}
