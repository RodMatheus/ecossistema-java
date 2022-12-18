package br.com.exemplo.comum.domain.service;

import br.com.exemplo.comum.api.v1.model.input.DepartamentoParam;
import br.com.exemplo.comum.domain.model.entities.Departamento;
import br.com.exemplo.comum.domain.model.entities.LogAuditoria;
import br.com.exemplo.comum.domain.repository.DepartamentoRepository;
import br.com.exemplo.comum.domain.repository.LogAuditoriaRepository;
import br.com.exemplo.comum.infrastructure.util.SecurityUtil;
import br.com.exemplo.comum.infrastructure.util.Utilitarios;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;
    private final LogAuditoriaRepository logAuditoriaRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository,
                               LogAuditoriaRepository logAuditoriaRepository){
        this.departamentoRepository = departamentoRepository;
        this.logAuditoriaRepository = logAuditoriaRepository;
    }
    @Transactional
    public void cadastraDepartamento(final DepartamentoParam departamentoParam) {
        log.info("Gerando entidade de departamento para cadastro.");
        final Departamento departamento = Departamento.of(departamentoParam.nome());

        log.info("Cadastrando o departamento na base. DEPARTAMENTO: {}.", departamento);
        departamentoRepository.save(departamento);

        log.info("Gerando log de transação.");
        LogAuditoria logAuditoria = LogAuditoria.ofInclusao(SecurityUtil.getUsuarioLogado(),
                Utilitarios.convertEntityLog(departamento));
        logAuditoriaRepository.save(logAuditoria);
    }
}