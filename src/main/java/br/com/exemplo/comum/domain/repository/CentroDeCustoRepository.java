package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.entities.CentroDeCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CentroDeCustoRepository  extends JpaRepository<CentroDeCusto, Long>, CentroDeCustoRepositoryCustom {
    Optional<CentroDeCusto> findByIdAndRemovidoIsFalse(final Long id);
}