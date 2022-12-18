package br.com.exemplo.comum.domain.model.converters;

import br.com.exemplo.comum.domain.model.constants.OperacaoAuditoria;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class OperacaoAuditoriaConverter implements AttributeConverter<OperacaoAuditoria, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OperacaoAuditoria operacao) {
        return operacao.getCodigo();
    }

    @Override
    public OperacaoAuditoria convertToEntityAttribute(Integer codigo) {
        return OperacaoAuditoria.byCodigo(codigo);
    }
}