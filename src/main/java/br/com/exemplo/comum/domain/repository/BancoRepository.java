package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.entities.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Long>, BancoRepositoryCustom {

    @Query("SELECT COUNT(b.id) > 0 FROM Banco b " +
            "WHERE b.codigo = :codigo " +
            "OR unaccent(b.nome) = unaccent(:nome)" )
    boolean validaParaCadastro(String codigo, String nome);

    @Query("SELECT COUNT(b.id) > 0 FROM Banco b " +
            "WHERE b.id <> :id " +
            "AND (unaccent(b.nome) = unaccent(:nome) or b.codigo = :codigo)")
    boolean validaParaAtualizacao(String codigo, String nome, Long id);
}