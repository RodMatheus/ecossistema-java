package br.com.exemplo.comum.domain.model.entities;

import br.com.exemplo.comum.api.v1.model.input.CentroDeCustoParam;
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
public class CentroDeCusto implements Serializable {

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

    public static CentroDeCusto of(final String nome) {
        CentroDeCusto centroDeCusto = new CentroDeCusto();
        centroDeCusto.setNome(nome);
        centroDeCusto.setAtivo(Boolean.TRUE);
        centroDeCusto.setRemovido(Boolean.FALSE);

        return centroDeCusto;
    }

    public static void ofAlteracao(CentroDeCusto centroDeCusto, final CentroDeCustoParam centroDeCustoParam) {
        centroDeCusto.setNome(centroDeCustoParam.nome());
    }

    public static void ofExclusao(CentroDeCusto centroDeCusto) {
        centroDeCusto.setAtivo(Boolean.FALSE);
        centroDeCusto.setRemovido(Boolean.TRUE);
    }

    public static void ofAtivo(CentroDeCusto centroDeCusto, final Boolean situacao) {
        centroDeCusto.setAtivo(situacao);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CentroDeCusto that = (CentroDeCusto) o;
        return ativo == that.ativo && removido == that.removido && id.equals(that.id) && nome.equals(that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, ativo, removido);
    }

    @Override
    public String toString() {
        return "CentroDeCusto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", ativo=" + ativo +
                ", removido=" + removido +
                '}';
    }
}