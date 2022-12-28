package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.entities.CentroDeCusto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CentroDeCustoRepository  extends JpaRepository<CentroDeCusto, Long>, CentroDeCustoRepositoryCustom {

    @Query("SELECT COUNT(c.id) > 0 FROM CentroDeCusto c " +
            "WHERE c.nome = :nome")
    boolean validaParaCadastro(String nome);

    @Query("SELECT COUNT(c.id) > 0 FROM CentroDeCusto c " +
            "WHERE c.nome = :nome " +
            "AND c.id <> :id")
    boolean validaParaAtualizacao(String nome, Long id);
}