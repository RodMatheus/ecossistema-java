package br.com.exemplo.comum.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlanoDeContasDTO(Long id, String nome, Boolean ativo, Set<PlanoDeContasDTO> filhos) {
}