package br.com.exemplo.comum.api.v1.mapper;

import br.com.exemplo.comum.api.v1.model.dto.PlanoDeContasDTO;
import br.com.exemplo.comum.domain.model.entities.PlanoDeContas;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PlanoDeContasMapper {

    PlanoDeContasDTO toResource(PlanoDeContas planoDeContas);

    List<PlanoDeContasDTO> toResourceList(List<PlanoDeContas> planosDeContas);
}