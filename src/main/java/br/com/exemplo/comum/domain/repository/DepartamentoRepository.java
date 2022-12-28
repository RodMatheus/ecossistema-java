package br.com.exemplo.comum.domain.repository;

import br.com.exemplo.comum.domain.model.entities.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartamentoRepository  extends JpaRepository<Departamento, Long>, DepartamentoRepositoryCustom {

    @Query("SELECT COUNT(d.id) > 0 FROM Departamento d " +
            "WHERE d.nome = :nome")
    boolean validaParaCadastro(String nome);

    @Query("SELECT COUNT(d.id) > 0 FROM Departamento d " +
            "WHERE d.nome = :nome " +
            "AND d.id <> :id")
    boolean validaParaAtualizacao(String nome, Long id);
}