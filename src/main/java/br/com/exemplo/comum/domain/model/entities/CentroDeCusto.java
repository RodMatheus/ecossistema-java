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
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
public class CentroDeCusto implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotNull
    private boolean removido;

    public static void ofExclusao(CentroDeCusto centroDeCusto) {
        centroDeCusto.setRemovido(Boolean.TRUE);
    }

    public static void ofAlteracao(CentroDeCusto centroDeCusto, final CentroDeCustoParam centroDeCustoParam) {
        centroDeCusto.setNome(centroDeCustoParam.nome());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CentroDeCusto that = (CentroDeCusto) o;
        return removido == that.removido && id.equals(that.id) && nome.equals(that.nome);
    }


    public static CentroDeCusto of (final String nome) {
        CentroDeCusto centroDeCusto = new CentroDeCusto();
        centroDeCusto.setNome(nome);
        centroDeCusto.setRemovido(Boolean.FALSE);

        return centroDeCusto;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, removido);
    }

    @Override
    public String toString() {
        return "CentroDeCusto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", removido=" + removido +
                '}';
    }
}