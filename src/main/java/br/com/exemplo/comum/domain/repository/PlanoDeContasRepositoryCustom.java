package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.api.v1.filter.FiltroPlanoDeContas;
import br.com.exemplo.comum.domain.model.entities.PlanoDeContas;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PlanoDeContasRepositoryCustom {

    Long contaPorFiltros(final FiltroPlanoDeContas filtros);
    List<PlanoDeContas> pesquisaPorFiltros(final FiltroPlanoDeContas filtros, final Pageable paginacao);
}