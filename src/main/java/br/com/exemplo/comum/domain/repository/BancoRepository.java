package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.entities.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Long>, BancoRepositoryCustom {

    @Query("SELECT COUNT(b.id) > 0 FROM Banco b " +
            "WHERE b.codigo = :codigo " +
            "OR b.nome = :nome")
    boolean validaParaCadastro(String codigo, String nome);

    @Query("SELECT COUNT(b.id) > 0 FROM Banco b " +
            "WHERE (b.codigo = :codigo " +
            "OR b.nome = :nome) " +
            "AND b.id <> :id")
    boolean validaParaAtualizacao(String codigo, String nome, Long id);
}