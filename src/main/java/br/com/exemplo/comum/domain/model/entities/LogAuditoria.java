package br.com.exemplo.comum.domain.model.entities;

import br.com.exemplo.comum.domain.model.constants.OperacaoAuditoria;
import br.com.exemplo.comum.domain.model.converters.OperacaoAuditoriaConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(schema = "auditoria", name = "log_auditoria")
@Getter
@Setter(value = AccessLevel.PRIVATE)
public class LogAuditoria implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String evento;

    @NotBlank
    private String entidade;

    @NotNull
    @Convert(converter = OperacaoAuditoriaConverter.class)
    private OperacaoAuditoria operacao;

    @NotNull
    @Column(name = "data_ocorrencia")
    private LocalDateTime dataOcorrencia;

    @NotBlank
    @Column(name = "usuario_logado")
    private String usuario;

    public static LogAuditoria ofInclusao(final String usuario, final String entidade) {

        LocalDateTime horario = LocalDateTime.now();
        StringBuilder evento = new StringBuilder();
        evento.append("Criação de registro de ponto às: ");
        evento.append(horario);
        evento.append(".");
        evento.append(" Operação realizada pelo usuário: ");
        evento.append(usuario);

        LogAuditoria log = new LogAuditoria();
        log.setOperacao(OperacaoAuditoria.INCLUSAO);
        log.setEvento(evento.toString());
        log.setDataOcorrencia(horario);
        log.setEntidade(entidade);
        log.setUsuario(usuario);

        return log;
    }

    public static LogAuditoria ofAlteracao(final String usuario, final String entidade) {

        LocalDateTime horario = LocalDateTime.now();
        StringBuilder evento = new StringBuilder();
        evento.append("Alteração de registro de ponto às: ");
        evento.append(horario);
        evento.append(".");
        evento.append(" Operação realizada pelo usuário: ");
        evento.append(usuario);

        LogAuditoria log = new LogAuditoria();
        log.setOperacao(OperacaoAuditoria.ALTERACAO);
        log.setEvento(evento.toString());
        log.setDataOcorrencia(horario);
        log.setEntidade(entidade);
        log.setUsuario(usuario);

        return log;
    }
}