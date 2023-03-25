package br.com.exemplo.comum.api.v1.mapper;

import br.com.exemplo.comum.api.v1.model.dto.DepartamentoDTO;
import br.com.exemplo.comum.domain.model.entities.Departamento;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepartamentoMapper {

    DepartamentoDTO toResource(Departamento departamento);

    List<DepartamentoDTO> toResourceList(List<Departamento> departamentos);
}