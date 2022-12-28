package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.entities.PlanoDeContas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanoDeContasRepository extends JpaRepository<PlanoDeContas, Long>, PlanoDeContasRepositoryCustom {

    @Query("SELECT COUNT(p.id) > 0 FROM PlanoDeContas p " +
            "WHERE p.nome = :nome " +
            "AND p.despesa = :despesa " +
            "AND p.pai = :pai " +
            "AND p.id <> :id")
    boolean validaParaAtualizacao(String nome, Boolean despesa, PlanoDeContas pai, Long id);

    @Query("SELECT COUNT(p.id) > 0 FROM PlanoDeContas p " +
            "WHERE p.nome = :nome " +
            "AND p.despesa = :despesa " +
            "AND p.pai = :pai")
    boolean validaParaCadastramento(String nome, Boolean despesa, PlanoDeContas pai);

    @Query("SELECT COUNT(p.id) > 0 FROM PlanoDeContas p " +
            "WHERE p.pai.id = :pai")
    boolean validaPai(Long pai);
}