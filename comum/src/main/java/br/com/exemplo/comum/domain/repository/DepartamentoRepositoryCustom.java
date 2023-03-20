package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.api.v1.filter.FiltroDepartamento;
import br.com.exemplo.comum.domain.model.entities.Departamento;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DepartamentoRepositoryCustom {

    Long contaPorFiltros(final FiltroDepartamento filtros);
    List<Departamento> pesquisaPorFiltros(final FiltroDepartamento filtros, final Pageable paginacao);
}
