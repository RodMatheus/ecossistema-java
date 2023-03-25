package br.com.exemplo.comum.domain.service;

import br.com.exemplo.comum.api.v1.model.input.DepartamentoParam;
import br.com.exemplo.comum.core.exception.RecursoNaoEncontradoException;
import br.com.exemplo.comum.core.exception.ValidacaoException;
import br.com.exemplo.comum.domain.model.entities.Departamento;
import br.com.exemplo.comum.domain.model.entities.LogAuditoria;
import br.com.exemplo.comum.domain.repository.DepartamentoRepository;
import br.com.exemplo.comum.domain.repository.LogAuditoriaRepository;
import br.com.exemplo.comum.infrastructure.util.MensagemUtil;
import br.com.exemplo.comum.infrastructure.util.SecurityUtil;
import br.com.exemplo.comum.infrastructure.util.Utilitarios;
import jakarta.persistence.LockModeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final LogAuditoriaRepository logAuditoriaRepository;
    private final MensagemUtil mensagemUtil;

    public DepartamentoService(DepartamentoRepository departamentoRepository,
                               LogAuditoriaRepository logAuditoriaRepository,
                               MensagemUtil mensagemUtil){
        this.departamentoRepository = departamentoRepository;
        this.logAuditoriaRepository = logAuditoriaRepository;
        this.mensagemUtil = mensagemUtil;

    }
    @Transactional
    public void cadastraDepartamento(final DepartamentoParam departamentoParam) {
        log.info("Verifica a existência do departamento na base.");
        this.validaCadastro(departamentoParam);

        log.info("Gerando entidade de departamento para cadastro.");
        final Departamento departamento = Departamento.of(departamentoParam.nome());

        log.info("Cadastrando o departamento na base. DEPARTAMENTO: {}.", departamento);
        departamentoRepository.save(departamento);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofInclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(departamento), Departamento.class);
        logAuditoriaRepository.save(logAuditoria);
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public Departamento atualizaDepartamento(final DepartamentoParam departamentoParam, final Long id) {
        log.info("Verificando a existência do departamento. ID: {}.", id);
        Departamento departamento = this.existeDepartamento(id);

        log.info("Verifica a existência do departamento na base com os novos dados.");
        this.validaAtualizacao(departamentoParam, id);

        log.info("Atualizando entidade de Departamento.");
        Departamento.ofAlteracao(departamento, departamentoParam);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofAlteracao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(departamento), Departamento.class);
        logAuditoriaRepository.save(logAuditoria);
        return departamento;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public void removeDepartamento(final Long id) {
        log.info("Verificando a existência do departamento. ID: {}.", id);
        Departamento departamento = this.existeDepartamento(id);

        log.info("Alterando status de removido da entidade.");
        Departamento.ofExclusao(departamento);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofExclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(departamento), Departamento.class);
        logAuditoriaRepository.save(logAuditoria);
    }

    public Departamento pesquisaDepartamentoPorId(final Long id) {
        log.info("Verificando a existência do departamento. ID: {}.", id);
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        mensagemUtil.mensagemPersonalizada("erro.departamento-nao-encontrado")));
    }

    private void validaCadastro(final DepartamentoParam departamentoParam) {
        if(departamentoRepository.validaParaCadastro(departamentoParam.nome())) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.departamento-cadastrado"));
        }
    }

    private void validaAtualizacao(final DepartamentoParam departamentoParam, final Long id) {
        if(departamentoRepository.validaParaAtualizacao(departamentoParam.nome(), id)) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.departamento-cadastrado"));
        }
    }

    private Departamento existeDepartamento(final Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        mensagemUtil.mensagemPersonalizada("erro.departamento-nao-encontrado")));
    }
}