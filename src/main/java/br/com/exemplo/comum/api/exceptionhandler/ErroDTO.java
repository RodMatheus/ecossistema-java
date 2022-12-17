package br.com.exemplo.comum.api.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.Set;

@Builder
@Value
@Schema(name = "Erro")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErroDTO {

    private String mensagem;

    @Schema(name = "validacoes", implementation = ValidacaoDTO.class)
    private Set<ValidacaoDTO> validacoes;

    public static ValidacaoDTO validacaoOf(final String campo, final String mensagem) {
        return ValidacaoDTO.builder()
                .campo(campo)
                .mensagem(mensagem)
                .build();
    }

    @Builder
    @Value
    public static class ValidacaoDTO {
        private String campo;
        private String mensagem;
    }
}
