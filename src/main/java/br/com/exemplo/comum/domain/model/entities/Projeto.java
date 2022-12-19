package br.com.exemplo.comum.domain.model.entities;

import br.com.exemplo.comum.api.v1.model.input.ProjetoParam;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
public class Projeto implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotNull
    private boolean removido;

    public static Projeto of(final String nome) {
        Projeto projeto = new Projeto();
        projeto.setNome(nome);
        projeto.setRemovido(Boolean.FALSE);

        return projeto;
    }

    public static void ofExclusao(Projeto projeto) {
        projeto.setRemovido(Boolean.TRUE);
    }

    public static void ofAlteracao(Projeto projeto, ProjetoParam projetoParam) {
        projeto.setNome(projetoParam.nome());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Projeto projeto = (Projeto) o;
        return removido == projeto.removido && id.equals(projeto.id) && nome.equals(projeto.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, removido);
    }

    @Override
    public String toString() {
        return "Projeto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", removido=" + removido +
                '}';
    }
}