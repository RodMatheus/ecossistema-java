package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.entities.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetoRepository  extends JpaRepository<Projeto, Long> {
}