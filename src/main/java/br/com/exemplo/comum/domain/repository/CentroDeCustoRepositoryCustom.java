package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.api.v1.filter.FiltroCentroDeCusto;
import br.com.exemplo.comum.domain.model.entities.CentroDeCusto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CentroDeCustoRepositoryCustom  {

    Long contaPorFiltros(final FiltroCentroDeCusto filtros);
    List<CentroDeCusto> pesquisaPorFiltros(final FiltroCentroDeCusto filtros, final Pageable paginacao);
}