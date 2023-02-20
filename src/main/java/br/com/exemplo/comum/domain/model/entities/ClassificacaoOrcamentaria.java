package br.com.exemplo.comum.domain.model.entities;

import br.com.exemplo.comum.api.v1.model.input.ClassificacaoOrcamentariaParam;
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
@Where(clause = "removido = false")
public class ClassificacaoOrcamentaria implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotNull
    private boolean despesa;

    @NotNull
    private boolean ativo;

    @NotNull
    private boolean removido;

    @JsonIgnore
    @JoinColumn(name = "pai")
    @ManyToOne(fetch = FetchType.LAZY)
    private ClassificacaoOrcamentaria pai;

    @JsonIgnore
    @OneToMany(mappedBy = "pai")
    private List<ClassificacaoOrcamentaria> filhos;

    public static ClassificacaoOrcamentaria of(final ClassificacaoOrcamentaria pai, final String nome, final Boolean despesa) {
        ClassificacaoOrcamentaria classificacaoOrcamentaria = new ClassificacaoOrcamentaria();
        classificacaoOrcamentaria.setNome(nome);
        classificacaoOrcamentaria.setDespesa(despesa);
        classificacaoOrcamentaria.setPai(pai);
        classificacaoOrcamentaria.setAtivo(Boolean.TRUE);
        classificacaoOrcamentaria.setRemovido(Boolean.FALSE);

        return classificacaoOrcamentaria;
    }

    public static void ofAlteracao(ClassificacaoOrcamentaria classificacaoOrcamentaria,
                                   final ClassificacaoOrcamentariaParam planoDeContasParam,
                                   final ClassificacaoOrcamentaria pai) {
        classificacaoOrcamentaria.setNome(planoDeContasParam.nome());
        classificacaoOrcamentaria.setDespesa(planoDeContasParam.despesa());
        classificacaoOrcamentaria.setPai(pai);
    }
    public static void ofExclusao(ClassificacaoOrcamentaria classificacaoOrcamentaria) {
        classificacaoOrcamentaria.setAtivo(Boolean.FALSE);
        classificacaoOrcamentaria.setRemovido(Boolean.TRUE);
    }

    public static void ofAtivo(ClassificacaoOrcamentaria classificacaoOrcamentaria, final Boolean situacao){
        classificacaoOrcamentaria.setAtivo(situacao);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassificacaoOrcamentaria that = (ClassificacaoOrcamentaria) o;
        return despesa == that.despesa && ativo == that.ativo && removido == that.removido
                && id.equals(that.id) && nome.equals(that.nome) && Objects.equals(pai, that.pai);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, despesa, ativo, removido, pai);
    }

    @Override
    public String toString() {
        return "ClassificacaoOrcamentaria{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", despesa=" + despesa +
                ", ativo=" + ativo +
                ", removido=" + removido +
                ", pai=" + pai +
                '}';
    }
}