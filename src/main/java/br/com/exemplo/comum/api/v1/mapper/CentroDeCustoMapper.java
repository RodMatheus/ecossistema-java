package br.com.exemplo.comum.api.v1.mapper;

import br.com.exemplo.comum.api.v1.model.dto.CentroDeCustoDTO;
import br.com.exemplo.comum.domain.model.entities.CentroDeCusto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CentroDeCustoMapper {

    CentroDeCustoDTO toResource(CentroDeCusto centroDeCusto);

    List<CentroDeCustoDTO> toResourceList(List<CentroDeCusto> centrosDeCusto);
}
