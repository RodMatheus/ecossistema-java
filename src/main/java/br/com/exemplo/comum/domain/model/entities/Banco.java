package br.com.exemplo.comum.domain.model.entities;

import br.com.exemplo.comum.api.v1.model.input.BancoParam;
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
    private boolean removido;

    public static Banco of (final String codigo, final String nome) {
        Banco banco = new Banco();
        banco.setCodigo(codigo);
        banco.setNome(nome);
        banco.setRemovido(Boolean.FALSE);

        return banco;
    }

    public static void ofExclusao(Banco banco) {
        banco.setRemovido(Boolean.TRUE);
    }

    public static void ofAlteracao(Banco banco, final BancoParam bancoParam) {
        banco.setCodigo(bancoParam.codigo());
        banco.setNome(bancoParam.nome());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banco banco = (Banco) o;
        return removido == banco.removido && id.equals(banco.id) && nome.equals(banco.nome) && codigo.equals(banco.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nome, codigo, removido);
    }

    @Override
    public String toString() {
        return "Banco{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", codigo='" + codigo + '\'' +
                ", removido=" + removido +
                '}';
    }
}