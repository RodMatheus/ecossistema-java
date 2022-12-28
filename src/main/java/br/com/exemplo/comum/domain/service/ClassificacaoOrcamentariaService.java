package br.com.exemplo.comum.domain.service;

import br.com.exemplo.comum.api.v1.model.input.ClassificacaoOrcamentariaParam;
import br.com.exemplo.comum.core.exception.RecursoNaoEncontradoException;
import br.com.exemplo.comum.core.exception.ValidacaoException;
import br.com.exemplo.comum.domain.model.entities.ClassificacaoOrcamentaria;
import br.com.exemplo.comum.domain.model.entities.LogAuditoria;
import br.com.exemplo.comum.domain.repository.ClassificacaoOrcamentariaRepository;
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
public class ClassificacaoOrcamentariaService {

    private final ClassificacaoOrcamentariaRepository classificacaoOrcamentariaRepository;
    private final LogAuditoriaRepository logAuditoriaRepository;
    private final MensagemUtil mensagemUtil;

    public ClassificacaoOrcamentariaService(ClassificacaoOrcamentariaRepository classificacaoOrcamentariaRepository,
                                            LogAuditoriaRepository logAuditoriaRepository,
                                            MensagemUtil mensagemUtil){
        this.classificacaoOrcamentariaRepository = classificacaoOrcamentariaRepository;
        this.logAuditoriaRepository = logAuditoriaRepository;
        this.mensagemUtil = mensagemUtil;

    }
    @Transactional
    public void cadastraClassificacaoOrcamentaria(final ClassificacaoOrcamentariaParam classificacaoOrcamentariaParam) {
        log.info("Verificando a existência do pai informado.");
        final ClassificacaoOrcamentaria pai = (classificacaoOrcamentariaParam.pai() == null)
                ? null : this.existePai(classificacaoOrcamentariaParam.pai());

        log.info("Verifica a existência da classificação orçamentária na base.");
        this.validaCadastro(classificacaoOrcamentariaParam.nome(), classificacaoOrcamentariaParam.despesa(), pai);

        log.info("Gerando entidade de classificação orçamentária para cadastro.");
        final ClassificacaoOrcamentaria classificacaoOrcamentaria = ClassificacaoOrcamentaria
                .of(pai, classificacaoOrcamentariaParam.nome(), classificacaoOrcamentariaParam.despesa());

        log.info("Cadastrando a classificação orçamentária na base." +
                " CLASSIFICAÇÃO ORÇAMENTARIA: {}.", classificacaoOrcamentaria);
        classificacaoOrcamentariaRepository.save(classificacaoOrcamentaria);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofInclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(classificacaoOrcamentaria), ClassificacaoOrcamentaria.class);
        logAuditoriaRepository.save(logAuditoria);
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public ClassificacaoOrcamentaria atualizaClassificacaoOrcamentaria(
            final ClassificacaoOrcamentariaParam classificacaoOrcamentariaParam, final Long id) {
        log.info("Verificando a existência da classificação orçamentária. ID: {}.", id);
        ClassificacaoOrcamentaria classificacaoOrcamentaria = this.existeClassificacao(id);

        log.info("Verificando a existência do pai informado.");
        final ClassificacaoOrcamentaria pai = this.existePai(classificacaoOrcamentariaParam.pai());

        log.info("Verifica a existência da classificação orçamentária na base com os novos dados.");
        this.validaAtualizacao(classificacaoOrcamentariaParam.nome(),
                classificacaoOrcamentariaParam.despesa(), pai, id);

        log.info("Atualizando entidade de classificação orçamentária.");
        ClassificacaoOrcamentaria.ofAlteracao(classificacaoOrcamentaria, classificacaoOrcamentariaParam, pai);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofAlteracao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(classificacaoOrcamentaria), ClassificacaoOrcamentaria.class);
        logAuditoriaRepository.save(logAuditoria);
        return classificacaoOrcamentaria;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public void removeClassificacaoOrcamentaria(final Long id) {
        log.info("Verificando a existência do classificação orçamentária. ID: {}.", id);
        ClassificacaoOrcamentaria classificacaoOrcamentaria = this.existeClassificacao(id);

        log.info("Verificando a existência de filhos no classificação orçamentária.");
        this.existemFilhos(id);

        log.info("Alterando status de removido da entidade.");
        ClassificacaoOrcamentaria.ofExclusao(classificacaoOrcamentaria);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofExclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(classificacaoOrcamentaria), ClassificacaoOrcamentaria.class);
        logAuditoriaRepository.save(logAuditoria);
    }

    public ClassificacaoOrcamentaria pesquisaClassificacaoOrcamentariaPorId(final Long id) {
        log.info("Verificando a existência da classificação orçamentária. ID: {}.", id);
        return classificacaoOrcamentariaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        mensagemUtil.mensagemPersonalizada("erro.classificacao-orcamentaria-nao-encontrada")));
    }

    private void validaCadastro(final String nome, final Boolean despesa, final ClassificacaoOrcamentaria pai) {
        if(classificacaoOrcamentariaRepository.validaParaCadastramento(nome, despesa, pai)) {
            throw new ValidacaoException(mensagemUtil
                    .mensagemPersonalizada("erro.classificacao-orcamentaria-cadastrada"));
        }
    }
    private void validaAtualizacao(final String nome, final Boolean despesa
            , final ClassificacaoOrcamentaria pai, final Long id) {
        if(classificacaoOrcamentariaRepository.validaParaAtualizacao(nome, despesa, pai, id)) {
            throw new ValidacaoException(mensagemUtil
                    .mensagemPersonalizada("erro.classificacao-orcamentaria-cadastrada"));
        }
    }

    private ClassificacaoOrcamentaria existeClassificacao(final Long id) {
        return classificacaoOrcamentariaRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException(
                        mensagemUtil.mensagemPersonalizada( "erro.classificacao-orcamentaria-nao-encontrada")));
    }

    private ClassificacaoOrcamentaria existePai(final Long id) {
        return classificacaoOrcamentariaRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException(
                        mensagemUtil
                                .mensagemPersonalizada("erro.classificacao-orcamentaria-pai-nao-encontrada")));
    }

    private void existemFilhos(final Long pai) {
        if(classificacaoOrcamentariaRepository.validaPai(pai)) {
            throw new ValidacaoException(mensagemUtil
                    .mensagemPersonalizada("erro.classificacao-orcamentaria-possui-filhos"));
        }
    }
}