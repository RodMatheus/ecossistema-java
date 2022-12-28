package br.com.exemplo.comum.domain.service;

import br.com.exemplo.comum.api.v1.model.input.BancoParam;
import br.com.exemplo.comum.core.exception.RecursoNaoEncontradoException;
import br.com.exemplo.comum.core.exception.ValidacaoException;
import br.com.exemplo.comum.domain.model.entities.Banco;
import br.com.exemplo.comum.domain.model.entities.LogAuditoria;
import br.com.exemplo.comum.domain.repository.BancoRepository;
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
public class BancoService {

    private final BancoRepository bancoRepository;
    private final LogAuditoriaRepository logAuditoriaRepository;
    private final MensagemUtil mensagemUtil;

    public BancoService(BancoRepository bancoRepository,
                        LogAuditoriaRepository logAuditoriaRepository,
                        MensagemUtil mensagemUtil){
        this.bancoRepository = bancoRepository;
        this.logAuditoriaRepository = logAuditoriaRepository;
        this.mensagemUtil = mensagemUtil;

    }
    @Transactional
    public void cadastraBanco(final BancoParam bancoParam) {
        log.info("Verifica a existência do banco na base.");
        this.validaCadastro(bancoParam);

        log.info("Gerando entidade de banco para cadastro.");
        final Banco banco = Banco.of(bancoParam.codigo(), bancoParam.nome());

        log.info("Cadastrando o banco na base. BANCO: {}.", banco);
        bancoRepository.save(banco);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofInclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(banco), Banco.class);
        logAuditoriaRepository.save(logAuditoria);
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public Banco atualizaBanco(final BancoParam bancoParam, final Long id) {
        log.info("Verificando a existência do BANCO. ID: {}.", id);
        Banco banco = this.existeBanco(id);

        log.info("Verifica a existência do banco na base com os novos dados.");
        this.validaAtualizacao(bancoParam, id);

        log.info("Atualizando entidade de Banco.");
        Banco.ofAlteracao(banco, bancoParam);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofAlteracao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(banco), Banco.class);
        logAuditoriaRepository.save(logAuditoria);
        return banco;
    }

    @Transactional
    @Lock(LockModeType.PESSIMISTIC_READ)
    public void removeBanco(final Long id) {
        log.info("Verificando a existência do Banco. ID: {}.", id);
        Banco banco = this.existeBanco(id);

        log.info("Alterando status de removido da entidade.");
        Banco.ofExclusao(banco);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofExclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(banco), Banco.class);
        logAuditoriaRepository.save(logAuditoria);
    }

    public Banco pesquisaBancoPorId(final Long id) {
        log.info("Verificando a existência do banco. ID: {}.", id);
        return bancoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        mensagemUtil.mensagemPersonalizada("erro.banco-nao-encontrado")));
    }

    private void validaCadastro(final BancoParam bancoParam) {
        if(bancoRepository.validaParaCadastro(bancoParam.codigo(), bancoParam.nome())) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.banco-cadastrado"));
        }
    }

    private void validaAtualizacao(final BancoParam bancoParam, final Long id) {
        if(bancoRepository.validaParaAtualizacao(bancoParam.codigo(), bancoParam.nome(), id)) {
            throw new ValidacaoException(mensagemUtil.mensagemPersonalizada("erro.banco-cadastrado"));
        }
    }

    private Banco existeBanco(final Long id) {
        return bancoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        mensagemUtil.mensagemPersonalizada("erro.banco-nao-encontrado")));
    }
}