package br.com.exemplo.comum.api.v1.mapper;

import br.com.exemplo.comum.api.v1.model.dto.ClassificacaoOrcamentariaDTO;
import br.com.exemplo.comum.domain.model.entities.ClassificacaoOrcamentaria;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClassificacaoOrcamentariaMapper {

    ClassificacaoOrcamentariaDTO toResource(ClassificacaoOrcamentaria classificacaoOrcamentaria);

    List<ClassificacaoOrcamentariaDTO> toResourceList(List<ClassificacaoOrcamentaria> classificacoesOrcamentarias);
}