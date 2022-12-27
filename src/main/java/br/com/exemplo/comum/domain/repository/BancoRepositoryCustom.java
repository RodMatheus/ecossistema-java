package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.api.v1.filter.FiltroBanco;
import br.com.exemplo.comum.domain.model.entities.Banco;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BancoRepositoryCustom {

    Long contaPorFiltros(final FiltroBanco filtros);
    List<Banco> pesquisaPorFiltros(final FiltroBanco filtros, final Pageable paginacao);
}