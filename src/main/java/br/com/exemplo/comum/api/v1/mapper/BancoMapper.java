package br.com.exemplo.comum.api.v1.mapper;

import br.com.exemplo.comum.api.v1.model.dto.BancoDTO;
import br.com.exemplo.comum.domain.model.entities.Banco;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BancoMapper {

    BancoDTO toResource(Banco banco);

    List<BancoDTO> toResourceList(List<Banco> bancos);
}
