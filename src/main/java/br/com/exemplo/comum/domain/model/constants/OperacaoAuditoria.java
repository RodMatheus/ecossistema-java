package br.com.exemplo.comum.domain.model.constants;

import br.com.exemplo.comum.core.exception.ValidacaoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum OperacaoAuditoria {
    INCLUSAO(1, "Inclusão"),
    ALTERACAO(2, "Alteração"),
    EXCLUSAO(3, "Exclusão");

    private final Integer codigo;
    private final String nome;

    public static OperacaoAuditoria byNome(String nome) {
        return Arrays
                .stream(values())
                .filter(tipo -> tipo.nome.equalsIgnoreCase(nome))
                .findAny()
                .orElseThrow(() -> new ValidacaoException("O nome informado é inválido!"));
    }

    public static OperacaoAuditoria byCodigo(Integer codigo) {
        return Arrays
                .stream(values())
                .filter(tipo -> tipo.codigo.equals(codigo))
                .findAny()
                .orElseThrow(() -> new ValidacaoException("O código informado é inválido!"));
    }
}