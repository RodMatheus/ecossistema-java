package br.com.exemplo.comum.api.v1.controller;

import br.com.exemplo.comum.api.v1.filter.FiltroPlanoDeContas;
import br.com.exemplo.comum.api.v1.mapper.PlanoDeContasMapper;
import br.com.exemplo.comum.api.v1.model.dto.PlanoDeContasDTO;
import br.com.exemplo.comum.api.v1.model.input.PlanoDeContasParam;
import br.com.exemplo.comum.api.v1.openapi.PlanoDeContasControllerOpenApi;
import br.com.exemplo.comum.core.security.CheckSecurity;
import br.com.exemplo.comum.domain.model.entities.PlanoDeContas;
import br.com.exemplo.comum.domain.repository.PlanoDeContasRepository;
import br.com.exemplo.comum.domain.service.PlanoDeContasService;
import br.com.exemplo.comum.infrastructure.util.WebUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/planos-de-contas",
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class PlanoDeContasController implements PlanoDeContasControllerOpenApi {

    private final PlanoDeContasService planoDeContasService;
    private final PlanoDeContasRepository planoDeContasRepository;
    private final PlanoDeContasMapper planoDeContasMapper;

    public PlanoDeContasController(PlanoDeContasService planoDeContasService,
                                   PlanoDeContasRepository planoDeContasRepository,
                                   PlanoDeContasMapper planoDeContasMapper) {
        this.planoDeContasService = planoDeContasService;
        this.planoDeContasRepository = planoDeContasRepository;
        this.planoDeContasMapper = planoDeContasMapper;
    }

    @GetMapping
    @CheckSecurity.comum.all
    public ResponseEntity<List<PlanoDeContasDTO>> get(FiltroPlanoDeContas filtros,
                                                      @RequestParam(defaultValue = WebUtil.PAGE_DEFAULT) Integer page,
                                                      @RequestParam(defaultValue = WebUtil.SIZE_DEFAULT) Integer size) {
        log.info("LISTAGEM DE PLANOS DE CONTAS");
        List<PlanoDeContasDTO> planosDeContasDTO = List.of();

        log.info("Contando planos de contas por filtros. FILTROS: {}.", filtros);
        final Long total = planoDeContasRepository.contaPorFiltros(filtros);

        if(total > 0) {
            log.info("Pesquisando planos de contas.");
            final List<PlanoDeContas> planosDeContas = planoDeContasRepository
                    .pesquisaPorFiltros(filtros, PageRequest.of(page, size));

            log.info("Convertendo entidades de Planos de contas em DTO.");
            planosDeContasDTO = planoDeContasMapper.toResourceList(planosDeContas);
        }

        log.info("Retornando dtos de resposta. DTOS: {}.", planosDeContasDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(WebUtil.X_TOTAL_COUNT_HEADER, String.valueOf(total))
                .body(planosDeContasDTO);
    }

    @GetMapping("/{id}")
    @CheckSecurity.comum.all
    public ResponseEntity<PlanoDeContasDTO> getById(@PathVariable Long id) {
        log.info("LISTAGEM DE PLANO DE CONTAS POR ID");

        log.info("Iniciando processo de busca por ID. ID: {}.", id);
        final PlanoDeContas planoDeContas = planoDeContasService.pesquisaPlanoDeContasPorId(id);

        log.info("Convertendo entidade de plano de contas em DTO. PLANO DE CONTAS: {}.", planoDeContas);
        final PlanoDeContasDTO planoDeContasDTO = planoDeContasMapper.toResource(planoDeContas);

        log.info("Retornando resposta da operação.");
        return ResponseEntity.ok(planoDeContasDTO);
    }

    @PostMapping
    @CheckSecurity.comum.maintain
    public ResponseEntity<Void> post(@Valid @RequestBody PlanoDeContasParam planoDeContasParam) {
        log.info("CADASTRO DE PROJETOS");

        log.info("Iniciando processo de cadastramento do plano de contas. PLANO DE CONTAS: {}.", planoDeContasParam);
        planoDeContasService.cadastraPlanoDeContas(planoDeContasParam);

       log.info("Retornando resposta da operação.");
       return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    @CheckSecurity.comum.maintain
    public ResponseEntity<PlanoDeContasDTO> put(@PathVariable Long id,
                                                @Valid @RequestBody PlanoDeContasParam planoDeContasParam) {
        log.info("ATUALIZAÇÃO DE PLANOS DE CONTA");

        log.info("Iniciando processo de atualização do plano de conta. PLANO DE CONTAS: {}.", planoDeContasParam);
        final PlanoDeContas planoDeContas = planoDeContasService.atualizaPlanoDeContas(planoDeContasParam, id);

        log.info("Convertendo entidade de plano de contas em DTO. PLANO DE CONTAS: {}.", planoDeContas);
        final PlanoDeContasDTO planoDeContasDTODTO = planoDeContasMapper.toResource(planoDeContas);

        log.info("Retornando resposta da operação.");
        return ResponseEntity.ok(planoDeContasDTODTO);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity.comum.maintain
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("EXCLUSÃO DE PLANOS DE CONTAS");

        log.info("Iniciando processo de exclusão de plano de contas.");
        planoDeContasService.removePlanoDeContas(id);

        log.info("Retornando resposta da operação.");
        return ResponseEntity
                .noContent()
                .build();
    }
}