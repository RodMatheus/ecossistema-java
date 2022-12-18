package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.entities.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Long> {
}