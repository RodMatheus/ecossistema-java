package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.api.v1.filter.FiltroClassificacaoOrcamentaria;
import br.com.exemplo.comum.domain.model.entities.ClassificacaoOrcamentaria;

import java.util.List;

public interface ClassificacaoOrcamentariaRepositoryCustom {

    Long contaPorFiltros(final FiltroClassificacaoOrcamentaria filtros);
    List<ClassificacaoOrcamentaria> pesquisaPorFiltros(final FiltroClassificacaoOrcamentaria filtros);
}