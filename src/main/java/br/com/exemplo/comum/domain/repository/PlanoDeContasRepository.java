package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.entities.PlanoDeContas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanoDeContasRepository extends JpaRepository<PlanoDeContas, Long> {

    @Query("SELECT COUNT(c.id) > 0 FROM PlanoDeContas c " +
            "WHERE unaccent(lower(c.nome)) = unaccent(lower(:nome)) " +
            "AND c.despesa = :despesa " +
            "AND c.pai IS NULL " +
            "AND c.id <> :id")
    boolean validaParaAtualizacaoPai(String nome, Boolean despesa, Long id);

    @Query("SELECT COUNT(c.id) > 0 FROM PlanoDeContas c " +
            "WHERE unaccent(lower(c.nome)) = unaccent(lower(:nome)) " +
            "AND c.despesa = :despesa " +
            "AND c.pai = :pai " +
            "AND c.id <> :id")
    boolean validaParaAtualizacaoFilho(String nome, Boolean despesa, PlanoDeContas pai, Long id);

    @Query("SELECT COUNT(c.id) > 0 FROM PlanoDeContas c " +
            "WHERE unaccent(lower(c.nome)) = unaccent(lower(:nome)) " +
            "AND c.despesa = :despesa " +
            "AND c.pai IS NULL")
    boolean validaParaCadastramentoPai(String nome, Boolean despesa);

    @Query("SELECT COUNT(c.id) > 0 FROM PlanoDeContas c " +
            "WHERE unaccent(lower(c.nome)) = unaccent(lower(:nome)) " +
            "AND c.despesa = :despesa " +
            "AND c.pai =:pai")
    boolean validaParaCadastramentoFilho(String nome, Boolean despesa, PlanoDeContas pai);

    @Query("SELECT COUNT(p.id) > 0 FROM PlanoDeContas p " +
            "WHERE p.pai.id = :pai")
    boolean validaPai(Long pai);

    @Query("FROM PlanoDeContas p " +
            "LEFT JOIN FETCH p.filhos " +
            "WHERE p.pai is null " +
            "ORDER BY p.id ASC")
    List<PlanoDeContas> findAllPais();

    @Query("SELECT COUNT(p.id) FROM PlanoDeContas p " +
            "WHERE p.pai is null ")
    Long countAllPais();
}