package br.com.exemplo.comum.domain.service;

import br.com.exemplo.comum.api.v1.model.input.PlanoDeContasParam;
import br.com.exemplo.comum.core.exception.RecursoNaoEncontradoException;
import br.com.exemplo.comum.core.exception.ValidacaoException;
import br.com.exemplo.comum.domain.model.entities.LogAuditoria;
import br.com.exemplo.comum.domain.model.entities.PlanoDeContas;
import br.com.exemplo.comum.domain.repository.LogAuditoriaRepository;
import br.com.exemplo.comum.domain.repository.PlanoDeContasRepository;
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
public class PlanoDeContasService {

    private final PlanoDeContasRepository planoDeContasRepository;
    private final LogAuditoriaRepository logAuditoriaRepository;
    private final MensagemUtil mensagemUtil;

    public PlanoDeContasService(PlanoDeContasRepository planoDeContasRepository,
                                LogAuditoriaRepository logAuditoriaRepository,
                                MensagemUtil mensagemUtil){
        this.planoDeContasRepository = planoDeContasRepository;
        this.logAuditoriaRepository = logAuditoriaRepository;
        this.mensagemUtil = mensagemUtil;

    }
    @Transactional
    public void cadastraPlanoDeContas(final PlanoDeContasParam planoDeContasParam) {
        log.info("Verificando a existência do pai informado.");
        final PlanoDeContas pai = (planoDeContasParam.pai() == null) ? null : this.existePai(planoDeContasParam.pai());

        log.info("Verifica a existência do plano de contas na base.");
        this.validaCadastro(planoDeContasParam.nome(), planoDeContasParam.despesa(), pai);

        log.info("Gerando entidade de plano de contas para cadastro.");
        final PlanoDeContas planoDeContas = PlanoDeContas
                .of(pai, planoDeContasParam.nome(), planoDeContasParam.despesa());

        log.info("Cadastrando o plano de contas na base. PLANO DE CONTAS: {}.", planoDeContas);
        planoDeContasRepository.save(planoDeContas);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofInclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(planoDeContas), PlanoDeContas.class);
        logAuditoriaRepository.save(logAuditoria);
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public PlanoDeContas atualizaPlanoDeContas(final PlanoDeContasParam planoDeContasParam, final Long id) {
        log.info("Verificando a existência do plano de contas. ID: {}.", id);
        PlanoDeContas planoDeContas = this.existePlano(id);

        log.info("Verificando a existência do pai informado.");
        final PlanoDeContas pai = this.existePai(planoDeContasParam.pai());

        log.info("Verifica a existência do plano de contas na base com os novos dados.");
        this.validaAtualizacao(planoDeContasParam.nome(), planoDeContasParam.despesa(), pai, id);

        log.info("Atualizando entidade de plano de contas.");
        PlanoDeContas.ofAlteracao(planoDeContas, planoDeContasParam, pai);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofAlteracao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(planoDeContas), PlanoDeContas.class);
        logAuditoriaRepository.save(logAuditoria);
        return planoDeContas;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public void removePlanoDeContas(final Long id) {
        log.info("Verificando a existência do plano de contas. ID: {}.", id);
        PlanoDeContas planoDeContas = this.existePlano(id);

        log.info("Verificando a existência de filhos no plano de contas.");
        this.existemFilhos(id);

        log.info("Alterando status de removido da entidade.");
        PlanoDeContas.ofExclusao(planoDeContas);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofExclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(planoDeContas), PlanoDeContas.class);
        logAuditoriaRepository.save(logAuditoria);
    }

    public PlanoDeContas pesquisaPlanoDeContasPorId(final Long id) {
        log.info("Verificando a existência do plano de contas. ID: {}.", id);
        return planoDeContasRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        mensagemUtil.mensagemPersonalizada("erro.plano-de-contas-nao-encontrado")));
    }

    private void validaCadastro(final String nome, final Boolean despesa, final PlanoDeContas pai) {
        if(planoDeContasRepository.validaParaCadastramento(nome, despesa, pai)) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.plano-de-contas-cadastrado"));
        }
    }
    private void validaAtualizacao(final String nome, final Boolean despesa, final PlanoDeContas pai, final Long id) {
        if(planoDeContasRepository.validaParaAtualizacao(nome, despesa, pai, id)) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.plano-de-contas-cadastrado"));
        }
    }

    private PlanoDeContas existePlano(final Long id) {
        return planoDeContasRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException(
                        mensagemUtil.mensagemPersonalizada( "erro.plano-de-contas-nao-encontrado")));
    }

    private PlanoDeContas existePai(final Long id) {
        return planoDeContasRepository.findById(id)
                .orElseThrow(() -> new ValidacaoException(
                        mensagemUtil.mensagemPersonalizada("erro.plano-de-contas-pai-nao-encontrado")));
    }

    private void existemFilhos(final Long pai) {
        if(planoDeContasRepository.validaPai(pai)) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.plano-de-contas-possui-filhos"));
        }
    }
}