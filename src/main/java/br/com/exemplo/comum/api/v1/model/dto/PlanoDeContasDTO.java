package br.com.exemplo.comum.api.v1.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.With;

import java.util.List;

@With
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PlanoDeContasDTO(Long id, String nome, String numeracao, Boolean ativo, List<PlanoDeContasDTO> filhos) { }