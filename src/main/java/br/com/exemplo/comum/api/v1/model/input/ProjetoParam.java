package br.com.exemplo.comum.api.v1.model.input;

import jakarta.validation.constraints.NotBlank;

public record ProjetoParam(
        @NotBlank
        String nome) {}
