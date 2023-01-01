package br.com.exemplo.comum.domain.service;

import br.com.exemplo.comum.api.v1.model.input.CentroDeCustoParam;
import br.com.exemplo.comum.core.exception.RecursoNaoEncontradoException;
import br.com.exemplo.comum.core.exception.ValidacaoException;
import br.com.exemplo.comum.domain.model.entities.CentroDeCusto;
import br.com.exemplo.comum.domain.model.entities.LogAuditoria;
import br.com.exemplo.comum.domain.repository.CentroDeCustoRepository;
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
public class CentroDeCustoService {

    private final CentroDeCustoRepository centroDeCustoRepository;
    private final LogAuditoriaRepository logAuditoriaRepository;
    private final MensagemUtil mensagemUtil;

    public CentroDeCustoService(CentroDeCustoRepository centroDeCustoRepository,
                                LogAuditoriaRepository logAuditoriaRepository,
                                MensagemUtil mensagemUtil){
        this.centroDeCustoRepository = centroDeCustoRepository;
        this.logAuditoriaRepository = logAuditoriaRepository;
        this.mensagemUtil = mensagemUtil;

    }
    @Transactional
    public void cadastraCentroDeCusto(final CentroDeCustoParam centroDeCustoParam) {
        log.info("Verifica a existência do centro de custo na base.");
        this.validaCadastro(centroDeCustoParam);

        log.info("Gerando entidade de centro de custo para cadastro.");
        final CentroDeCusto centroDeCusto = CentroDeCusto.of(centroDeCustoParam.nome());

        log.info("Cadastrando o centro de custo na base. CENTRO DE CUSTO: {}.", centroDeCusto);
        centroDeCustoRepository.save(centroDeCusto);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofInclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(centroDeCusto), CentroDeCusto.class);
        logAuditoriaRepository.save(logAuditoria);
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public CentroDeCusto atualizaCentroDeCusto(final CentroDeCustoParam centroDeCustoParam, final Long id) {
        log.info("Verificando a existência do centro de custo. ID: {}.", id);
        CentroDeCusto centroDeCusto = this.existeCentroDeCusto(id);

        log.info("Verifica a existência do centro de custo na base com os novos dados.");
        this.validaAtualizacao(centroDeCustoParam, id);

        log.info("Atualizando entidade de Centro de custo.");
        CentroDeCusto.ofAlteracao(centroDeCusto, centroDeCustoParam);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofAlteracao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(centroDeCusto), CentroDeCusto.class);
        logAuditoriaRepository.save(logAuditoria);
        return centroDeCusto;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public void removeCentroDeCusto(final Long id) {
        log.info("Verificando a existência do centro de custo. ID: {}.", id);
        CentroDeCusto centroDeCusto = this.existeCentroDeCusto(id);

        log.info("Alterando status de removido da entidade.");
        CentroDeCusto.ofExclusao(centroDeCusto);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofExclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(centroDeCusto), CentroDeCusto.class);
        logAuditoriaRepository.save(logAuditoria);
    }

    public CentroDeCusto pesquisaCentroDeCustoPorId(final Long id) {
        log.info("Verificando a existência do centro de custo. ID: {}.", id);
        return centroDeCustoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        mensagemUtil.mensagemPersonalizada("erro.centro-custo-nao-encontrado")));
    }

    private void validaCadastro(final CentroDeCustoParam centroDeCustoParam) {
        if(centroDeCustoRepository.validaParaCadastro(centroDeCustoParam.nome())) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.centro-custo-cadastrado"));
        }
    }

    private void validaAtualizacao(final CentroDeCustoParam centroDeCustoParam, final Long id) {
        if(centroDeCustoRepository.validaParaAtualizacao(centroDeCustoParam.nome(), id)) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.centro-custo-cadastrado"));
        }
    }

    private CentroDeCusto existeCentroDeCusto(final Long id) {
        return centroDeCustoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        mensagemUtil.mensagemPersonalizada("erro.centro-custo-nao-encontrado")));
    }
}