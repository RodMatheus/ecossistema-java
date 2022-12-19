package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.entities.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartamentoRepository  extends JpaRepository<Departamento, Long>, DepartamentoRepositoryCustom {
    Optional<Departamento> findByIdAndRemovidoIsFalse(final Long id);
}