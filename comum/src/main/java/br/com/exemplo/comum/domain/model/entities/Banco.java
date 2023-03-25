package br.com.exemplo.comum.domain.model.entities;

import br.com.exemplo.comum.api.v1.model.input.PutBanco;
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
public class Banco implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String nome;

    @NotBlank
    private String codigo;


    @NotNull
    private boolean ativo;

    @NotNull
    private boolean removido;

    public static Banco of(final String codigo, final String nome) {
        Banco banco = new Banco();
        banco.setCodigo(codigo);
        banco.setNome(nome);
        banco.setAtivo(Boolean.TRUE);
        banco.setRemovido(Boolean.FALSE);

        return banco;
    }

    public static void ofAlteracao(Banco banco, final PutBanco putBanco) {
        banco.setCodigo(putBanco.codigo());
        banco.setNome(putBanco.nome());
    }

    public static void ofExclusao(Banco banco) {
        banco.setAtivo(Boolean.FALSE);
        banco.setRemovido(Boolean.TRUE);
    }

    public static void ofAtivo(Banco banco, final Boolean situacao) {
        banco.setAtivo(situacao);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banco banco = (Banco) o;
        return ativo == banco.ativo && removido == banco.removido && id.equals(banco.id) && nome.equals(banco.nome) && codigo.equals(banco.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, codigo, ativo, removido);
    }

    @Override
    public String toString() {
        return "Banco{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", codigo='" + codigo + '\'' +
                ", ativo=" + ativo +
                ", removido=" + removido +
                '}';
    }
}