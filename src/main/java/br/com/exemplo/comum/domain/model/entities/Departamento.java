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
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
public class Departamento implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String descricao;

    @NotNull
    private boolean removido;

    public static Departamento of(final String nome) {
        Departamento departamento = new Departamento();
        departamento.setDescricao(nome);
        departamento.setRemovido(Boolean.FALSE);

        return departamento;
    }

    public static void ofExclusao(Departamento departamento) {
        departamento.setRemovido(Boolean.TRUE);
    }

    public static void ofUpdate(Departamento departamento, DepartamentoParam departamentoParam) {
        departamento.setDescricao(departamentoParam.nome());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Departamento that = (Departamento) o;
        return removido == that.removido && id.equals(that.id) && descricao.equals(that.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, descricao, removido);
    }

    @Override
    public String toString() {
        return "Departamento{" +
                "id=" + id +
                ", descricao='" + descricao + '\'' +
                ", removido=" + removido +
                '}';
    }
}