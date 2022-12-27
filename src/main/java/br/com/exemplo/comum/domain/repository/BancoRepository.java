package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.entities.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Long>, BancoRepositoryCustom {
    Optional<Banco> findByIdAndRemovidoIsFalse(Long id);

    boolean existsByCodigoAndNomeAndIdIsNotAndRemovidoIsFalse(String codigo, String nome, Long id);

    boolean existsByCodigoAndNomeAndRemovidoIsFalse(String codigo, String nome);
}