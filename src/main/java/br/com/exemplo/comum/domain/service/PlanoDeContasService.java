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
import org.springframework.util.CollectionUtils;

import java.util.List;

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
        log.info("Iniciando validações e recuperação de pai.");
        final PlanoDeContas pai = this.validaCadastro(planoDeContasParam);

        log.info("Gerando entidade de plano de contas para cadastro.");
        final PlanoDeContas planoDeContas = PlanoDeContas.of(pai, planoDeContasParam.nome(), planoDeContasParam.despesa());

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

        log.info("Iniciando validações e recuperação de pai.");
        final PlanoDeContas pai = this.validaAtualizacao(planoDeContasParam, id);

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
    public PlanoDeContas inativaPlanoDeContas(final Long id) {
        log.info("Verificando a existência do plano de contas. ID: {}.", id);
        PlanoDeContas planoDeContas = this.existePlano(id);

        log.info("Inativando entidade(s) de plano de contas.");
        PlanoDeContas.ofAtivo(planoDeContas, Boolean.FALSE);

        log.info("Inativando entidade(s) filhas de plano de contas.");
        this.alteraStatusAtivoFilhos(planoDeContas.getFilhos(), Boolean.FALSE);

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

    private void alteraStatusAtivoFilhos(List<PlanoDeContas> planosDeContas, Boolean status) {
        for (PlanoDeContas plano : planosDeContas) {
            log.info("Alterando status de ativação de plano de contas.");
            PlanoDeContas.ofAtivo(plano, status);

            log.info("Gerando log de transação.");
            LogAuditoria logAuditoria = LogAuditoria.ofAlteracao(SecurityUtil.getUsuarioLogado(),
                    Utilitarios.convertEntityLog(plano), PlanoDeContas.class);
            logAuditoriaRepository.save(logAuditoria);

            log.info("Alterando status de ativação para filhos.");
            if(!CollectionUtils.isEmpty(plano.getFilhos())) {
                this.alteraStatusAtivoFilhos(plano.getFilhos(), status);
            }
        }
    }

    private PlanoDeContas validaCadastro(final PlanoDeContasParam planoDeContasParam) {
        PlanoDeContas pai = null;
        if(planoDeContasParam.pai() == null) {
            this.validaCadastroPai(planoDeContasParam.nome(), planoDeContasParam.despesa());
        } else {
            pai = existePai(planoDeContasParam.pai());
            this.validaCadastroFilho(planoDeContasParam.nome(), planoDeContasParam.despesa(), pai);
        }

        return pai;
    }

    private void validaCadastroPai(final String nome, final Boolean despesa) {
        if(planoDeContasRepository.validaParaCadastramentoPai(nome, despesa)) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.plano-de-contas-pai-cadastrado"));
        }
    }

    private void validaCadastroFilho(final String nome, final Boolean despesa, final PlanoDeContas pai) {
        if(planoDeContasRepository.validaParaCadastramentoFilho(nome, despesa, pai)) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.plano-de-contas-filho-cadastrado"));
        }
    }
    private PlanoDeContas validaAtualizacao(final PlanoDeContasParam planoDeContasParam, final Long id) {
        PlanoDeContas pai = null;
        if(planoDeContasParam.pai() == null) {
            this.validaAtualizacaoPai(planoDeContasParam.nome(), planoDeContasParam.despesa(), id);
        } else {
            pai = this.existePai(planoDeContasParam.pai());
            this.validaAtualizacaoFilho(planoDeContasParam.nome(), planoDeContasParam.despesa(), pai, id);
        }

        return pai;
    }
    private void validaAtualizacaoPai(final String nome, final Boolean despesa, final Long id) {
        if(planoDeContasRepository.validaParaAtualizacaoPai(nome, despesa, id)) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.plano-de-contas-pai-cadastrado"));
        }
    }

    private void validaAtualizacaoFilho(final String nome, final Boolean despesa, final PlanoDeContas pai, final Long id) {
        if(planoDeContasRepository.validaParaAtualizacaoFilho(nome, despesa, pai, id)) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.plano-de-contas-filho-cadastrado"));
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