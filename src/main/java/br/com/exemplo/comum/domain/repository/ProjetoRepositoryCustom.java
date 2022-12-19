package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.api.v1.filter.FiltroProjeto;
import br.com.exemplo.comum.domain.model.entities.Projeto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjetoRepositoryCustom {

    Long contaPorFiltros(final FiltroProjeto filtros);
    List<Projeto> pesquisaPorFiltros(final FiltroProjeto filtros, final Pageable paginacao);
}
