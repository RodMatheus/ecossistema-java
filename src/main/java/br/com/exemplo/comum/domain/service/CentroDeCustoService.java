package br.com.exemplo.comum.domain.service;

import br.com.exemplo.comum.api.v1.model.input.CentroDeCustoParam;
import br.com.exemplo.comum.core.exception.RecursoNaoEncontradoException;
import br.com.exemplo.comum.domain.model.entities.CentroDeCusto;
import br.com.exemplo.comum.domain.model.entities.LogAuditoria;
import br.com.exemplo.comum.domain.repository.CentroDeCustoRepository;
import br.com.exemplo.comum.domain.repository.LogAuditoriaRepository;
import br.com.exemplo.comum.infrastructure.util.MensagemUtil;
import br.com.exemplo.comum.infrastructure.util.SecurityUtil;
import br.com.exemplo.comum.infrastructure.util.Utilitarios;
import lombok.extern.slf4j.Slf4j;
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
    public void removeCentroDeCusto(final Long id) {
        log.info("Verificando a existência do centro de custo. ID: {}.", id);
        CentroDeCusto centroDeCusto = centroDeCustoRepository.findByIdAndRemovidoIsFalse(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        mensagemUtil.mensagemPersonalizada("erro.centro-custo-nao-encontrado")));

        log.info("Alterando status de removido da entidade.");
        CentroDeCusto.ofExclusao(centroDeCusto);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofExclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(centroDeCusto), CentroDeCusto.class);
        logAuditoriaRepository.save(logAuditoria);
    }

    @Transactional
    public CentroDeCusto atualizaCentroDeCusto(final CentroDeCustoParam centroDeCustoParam, final Long id) {
        log.info("Verificando a existência do centro de custo. ID: {}.", id);
        CentroDeCusto centroDeCusto = centroDeCustoRepository.findByIdAndRemovidoIsFalse(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        mensagemUtil.mensagemPersonalizada("erro.centro-custo-nao-encontrado")));

        log.info("Atualizando entidade de Centro de custo.");
        CentroDeCusto.ofAlteracao(centroDeCusto, centroDeCustoParam);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofAlteracao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(centroDeCusto), CentroDeCusto.class);
        logAuditoriaRepository.save(logAuditoria);
        return centroDeCusto;
    }

    public CentroDeCusto pesquisaCentroDeCustoPorId(final Long id) {
        log.info("Verificando a existência do centro de custo. ID: {}.", id);
        return centroDeCustoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        mensagemUtil.mensagemPersonalizada("erro.centro-custo-nao-encontrado")));
    }
}