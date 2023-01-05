package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.entities.Projeto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjetoRepository  extends JpaRepository<Projeto, Long>, ProjetoRepositoryCustom {

    @Query("SELECT COUNT(p.id) > 0 FROM Projeto p " +
            "WHERE unaccent(lower(p.nome)) = unaccent(lower(:nome)) ")
    boolean validaParaCadastro(String nome);

    @Query("SELECT COUNT(p.id) > 0 FROM Projeto p " +
            "WHERE p.nome = :nome " +
            "AND p.id <> :id")
    boolean validaParaAtualizacao(String nome, Long id);
}