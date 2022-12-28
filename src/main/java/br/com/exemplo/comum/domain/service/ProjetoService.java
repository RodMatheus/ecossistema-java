package br.com.exemplo.comum.domain.service;

import br.com.exemplo.comum.api.v1.model.input.ProjetoParam;
import br.com.exemplo.comum.core.exception.RecursoNaoEncontradoException;
import br.com.exemplo.comum.core.exception.ValidacaoException;
import br.com.exemplo.comum.domain.model.entities.Departamento;
import br.com.exemplo.comum.domain.model.entities.LogAuditoria;
import br.com.exemplo.comum.domain.model.entities.Projeto;
import br.com.exemplo.comum.domain.repository.LogAuditoriaRepository;
import br.com.exemplo.comum.domain.repository.ProjetoRepository;
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
public class ProjetoService {

    private final ProjetoRepository projetoRepository;
    private final LogAuditoriaRepository logAuditoriaRepository;
    private final MensagemUtil mensagemUtil;

    public ProjetoService(ProjetoRepository projetoRepository,
                          LogAuditoriaRepository logAuditoriaRepository,
                          MensagemUtil mensagemUtil){
        this.projetoRepository = projetoRepository;
        this.logAuditoriaRepository = logAuditoriaRepository;
        this.mensagemUtil = mensagemUtil;

    }
    @Transactional
    public void cadastraProjeto(final ProjetoParam projetoParam) {
        log.info("Verifica a existência do projeto na base.");
        this.validaCadastro(projetoParam);

        log.info("Gerando entidade de projeto para cadastro.");
        final Projeto projeto = Projeto.of(projetoParam.nome());

        log.info("Cadastrando o projeto na base. PROJETO: {}.", projeto);
        projetoRepository.save(projeto);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofInclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(projeto), Projeto.class);
        logAuditoriaRepository.save(logAuditoria);
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public Projeto atualizaProjeto(final ProjetoParam projetoParam, final Long id) {
        log.info("Verificando a existência do projeto. ID: {}.", id);
        Projeto projeto = this.existeProjeto(id);

        log.info("Verifica a existência do projeto na base com os novos dados.");
        this.validaAtualizacao(projetoParam, id);

        log.info("Atualizando entidade de Projeto.");
        Projeto.ofAlteracao(projeto, projetoParam);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofAlteracao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(projeto), Departamento.class);
        logAuditoriaRepository.save(logAuditoria);
        return projeto;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public void removeProjeto(final Long id) {
        log.info("Verificando a existência do projeto. ID: {}.", id);
        Projeto projeto = this.existeProjeto(id);

        log.info("Alterando status de removido da entidade.");
        Projeto.ofExclusao(projeto);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofExclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(projeto), Departamento.class);
        logAuditoriaRepository.save(logAuditoria);
    }

    public Projeto pesquisaProjetoPorId(final Long id) {
        log.info("Verificando a existência do projeto. ID: {}.", id);
        return projetoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        mensagemUtil.mensagemPersonalizada("erro.projeto-nao-encontrado")));
    }

    private void validaCadastro(final ProjetoParam projetoParam) {
        if(projetoRepository.validaParaCadastro(projetoParam.nome())) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.projeto-cadastrado"));
        }
    }

    private void validaAtualizacao(final ProjetoParam projetoParam, final Long id) {
        if(projetoRepository.validaParaAtualizacao(projetoParam.nome(), id)) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.projeto-cadastrado"));
        }
    }

    private Projeto existeProjeto(final Long id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        mensagemUtil.mensagemPersonalizada("erro.projeto-nao-encontrado")));
    }
}