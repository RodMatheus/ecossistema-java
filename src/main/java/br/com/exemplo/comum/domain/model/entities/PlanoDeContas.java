package br.com.exemplo.comum.domain.model.entities;

import br.com.exemplo.comum.api.v1.model.input.PlanoDeContasParam;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter(AccessLevel.PRIVATE)
public class PlanoDeContas implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotNull
    private boolean despesa;

    @NotNull
    private boolean removido;

    @JsonIgnore
    @JoinColumn(name = "pai")
    @ManyToOne(fetch = FetchType.LAZY)
    private PlanoDeContas pai;


    @JsonIgnore
    @JoinColumn(name = "pai")
    @OneToMany(fetch = FetchType.LAZY)
    @Where(clause = "removido = false")
    private List<PlanoDeContas> filhos;

    public static PlanoDeContas of(final PlanoDeContas pai, final String nome, final Boolean despesa) {
        PlanoDeContas plano = new PlanoDeContas();
        plano.setNome(nome);
        plano.setDespesa(despesa);
        plano.setPai(pai);
        plano.setRemovido(Boolean.FALSE);

        return plano;
    }

    public static void ofAlteracao(PlanoDeContas planoDeContas,
                                   final PlanoDeContasParam planoDeContasParam, final PlanoDeContas pai) {
        planoDeContas.setNome(planoDeContasParam.nome());
        planoDeContas.setDespesa(planoDeContasParam.despesa());
        planoDeContas.setPai(pai);
    }
    public static void ofExclusao(PlanoDeContas planoDeContas) {
        planoDeContas.setRemovido(Boolean.TRUE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanoDeContas that = (PlanoDeContas) o;
        return despesa == that.despesa && removido == that.removido && id.equals(that.id) && nome.equals(that.nome) && Objects.equals(pai, that.pai);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, despesa, removido, pai);
    }

    @Override
    public String toString() {
        return "PlanoDeContas{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", despesa=" + despesa +
                ", removido=" + removido +
                ", pai=" + pai +
                '}';
    }
}