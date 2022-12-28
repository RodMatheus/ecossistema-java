package br.com.exemplo.comum.domain.model.entities;

import br.com.exemplo.comum.api.v1.model.input.DepartamentoParam;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
@Where(clause = "removido = false")
public class Departamento implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotNull
    private boolean ativo;

    @NotNull
    private boolean removido;

    public static Departamento of(final String nome) {
        Departamento departamento = new Departamento();
        departamento.setNome(nome);
        departamento.setAtivo(Boolean.TRUE);
        departamento.setRemovido(Boolean.FALSE);

        return departamento;
    }

    public static void ofAlteracao(Departamento departamento, final DepartamentoParam departamentoParam) {
        departamento.setNome(departamentoParam.nome());
    }

    public static void ofExclusao(Departamento departamento) {
        departamento.setAtivo(Boolean.FALSE);
        departamento.setRemovido(Boolean.TRUE);
    }

    public static void ofAtivo(Departamento departamento, final Boolean situacao) {
        departamento.setAtivo(situacao);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Departamento that = (Departamento) o;
        return ativo == that.ativo && removido == that.removido && id.equals(that.id) && nome.equals(that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, ativo, removido);
    }

    @Override
    public String toString() {
        return "Departamento{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", ativo=" + ativo +
                ", removido=" + removido +
                '}';
    }
}