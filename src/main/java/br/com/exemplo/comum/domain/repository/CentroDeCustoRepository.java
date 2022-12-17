package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.CentroDeCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroDeCustoRepository  extends JpaRepository<CentroDeCusto, Long> {
}