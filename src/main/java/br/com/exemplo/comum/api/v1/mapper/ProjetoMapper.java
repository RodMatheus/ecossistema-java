package br.com.exemplo.comum.api.v1.mapper;

import br.com.exemplo.comum.api.v1.model.dto.ProjetoDTO;
import br.com.exemplo.comum.domain.model.entities.Projeto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjetoMapper {

    ProjetoDTO toResource(Projeto projeto);

    List<ProjetoDTO> toResourceList(List<Projeto> projetos);
}
