package br.com.exemplo.comum.api.v1.controller;

import br.com.exemplo.comum.api.v1.filter.FiltroDepartamento;
import br.com.exemplo.comum.api.v1.mapper.DepartamentoMapper;
import br.com.exemplo.comum.api.v1.model.dto.DepartamentoDTO;
import br.com.exemplo.comum.api.v1.model.input.DepartamentoParam;
import br.com.exemplo.comum.api.v1.openapi.DepartamentoControllerOpenApi;
import br.com.exemplo.comum.core.security.CheckSecurity;
import br.com.exemplo.comum.domain.model.entities.Departamento;
import br.com.exemplo.comum.domain.repository.DepartamentoRepository;
import br.com.exemplo.comum.domain.service.DepartamentoService;
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
@RequestMapping(value = "/departamentos",
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class DepartamentoController implements DepartamentoControllerOpenApi {

    private final DepartamentoService departamentoService;
    private final DepartamentoRepository departamentoRepository;
    private final DepartamentoMapper departamentoMapper;
    public DepartamentoController(DepartamentoService departamentoService,
                                  DepartamentoRepository departamentoRepository,
                                  DepartamentoMapper departamentoMapper) {
        this.departamentoService = departamentoService;
        this.departamentoRepository = departamentoRepository;
        this.departamentoMapper = departamentoMapper;
    }

    @GetMapping
    @CheckSecurity.comum.all
    public ResponseEntity<List<DepartamentoDTO>> get(FiltroDepartamento filtros,
           @RequestParam Integer page, @RequestParam Integer size) {
        log.info("LISTAGEM DE DEPARTAMENTOS");
        List<DepartamentoDTO> departamentosDTO = List.of();

        log.info("Contando departamentos por filtros. FILTROS: {}.", filtros);
        final Long total = departamentoRepository.contaPorFiltros(filtros);

        if(total > 0) {
            log.info("Pesquisando departamentos.");
            final List<Departamento> departamentos = departamentoRepository
                    .pesquisaPorFiltros(filtros, PageRequest.of(page, size));

            log.info("Convertendo entidades de Departamento em DTO. DEPARTAMENTOS: {}.", departamentos);
            departamentosDTO = departamentoMapper.toResourceList(departamentos);
        }

        log.info("Retornando dtos de resposta. DTOS: {}.", departamentosDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(WebUtil.X_TOTAL_COUNT_HEADER, String.valueOf(total))
                .body(departamentosDTO);
    }

    @PostMapping
    @CheckSecurity.comum.maintain
    public ResponseEntity<Void> post(@Valid @RequestBody DepartamentoParam departamentoParam) {
        log.info("CADASTRO DE DEPARTAMENTOS");

        log.info("Iniciando processo de cadastramento do departamento. DEPARTAMENTO: {}.", departamentoParam);
        departamentoService.cadastraDepartamento(departamentoParam);

       log.info("Retornando resposta da operação.");
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping("/{id}")
    @CheckSecurity.comum.maintain
    public ResponseEntity<Void> DELETE(@PathVariable Long id) {
        log.info("EXCLUSÃO DE DEPARTAMENTOS");

        log.info("Iniciando processo de exclusão de departamento.");
        departamentoService.removeDepartamento(id);

        log.info("Retornando resposta da operação.");
        return ResponseEntity
                .noContent()
                .build();
    }
}