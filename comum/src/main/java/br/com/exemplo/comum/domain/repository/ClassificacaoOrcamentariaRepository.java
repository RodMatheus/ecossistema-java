package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.entities.ClassificacaoOrcamentaria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassificacaoOrcamentariaRepository extends JpaRepository<ClassificacaoOrcamentaria, Long> {

    @Query("SELECT COUNT(c.id) > 0 FROM ClassificacaoOrcamentaria c " +
            "WHERE unaccent(lower(c.nome)) = unaccent(lower(:nome)) " +
            "AND c.despesa = :despesa " +
            "AND c.pai IS NULL " +
            "AND c.id <> :id")
    boolean validaParaAtualizacaoPai(String nome, Boolean despesa, Long id);

    @Query("SELECT COUNT(c.id) > 0 FROM ClassificacaoOrcamentaria c " +
            "WHERE unaccent(lower(c.nome)) = unaccent(lower(:nome)) " +
            "AND c.despesa = :despesa " +
            "AND c.pai = :pai " +
            "AND c.id <> :id")
    boolean validaParaAtualizacaoFilho(String nome, Boolean despesa, ClassificacaoOrcamentaria pai, Long id);

    @Query("SELECT COUNT(c.id) > 0 FROM ClassificacaoOrcamentaria c " +
            "WHERE unaccent(lower(c.nome)) = unaccent(lower(:nome)) " +
            "AND c.despesa = :despesa " +
            "AND c.pai IS NULL")
    boolean validaParaCadastramentoPai(String nome, Boolean despesa);

    @Query("SELECT COUNT(c.id) > 0 FROM ClassificacaoOrcamentaria c " +
            "WHERE unaccent(lower(c.nome)) = unaccent(lower(:nome)) " +
            "AND c.despesa = :despesa " +
            "AND c.pai =:pai")
    boolean validaParaCadastramentoFilho(String nome, Boolean despesa, ClassificacaoOrcamentaria pai);

    @Query("SELECT COUNT(c.id) > 0 FROM ClassificacaoOrcamentaria c " +
            "WHERE c.pai.id = :pai")
    boolean validaPai(Long pai);

    @Query(" FROM ClassificacaoOrcamentaria c " +
            "LEFT JOIN FETCH c.filhos " +
            "WHERE c.pai is null " +
            "ORDER BY c.id ASC")
    List<ClassificacaoOrcamentaria> findAllPais();

    @Query("SELECT COUNT(c.id) FROM ClassificacaoOrcamentaria c " +
            "WHERE c.pai is null ")
    Long countAllPais();
}