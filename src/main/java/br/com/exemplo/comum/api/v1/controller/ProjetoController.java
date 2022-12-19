package br.com.exemplo.comum.api.v1.controller;

import br.com.exemplo.comum.api.v1.filter.FiltroProjeto;
import br.com.exemplo.comum.api.v1.mapper.ProjetoMapper;
import br.com.exemplo.comum.api.v1.model.dto.ProjetoDTO;
import br.com.exemplo.comum.api.v1.model.input.ProjetoParam;
import br.com.exemplo.comum.api.v1.openapi.ProjetoControllerOpenApi;
import br.com.exemplo.comum.core.security.CheckSecurity;
import br.com.exemplo.comum.domain.model.entities.Projeto;
import br.com.exemplo.comum.domain.repository.ProjetoRepository;
import br.com.exemplo.comum.domain.service.ProjetoService;
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
@RequestMapping(value = "/projetos",
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class ProjetoController implements ProjetoControllerOpenApi {

    private final ProjetoService projetoService;
    private final ProjetoRepository projetoRepository;
    private final ProjetoMapper projetoMapper;

    public ProjetoController(ProjetoService projetoService,
                             ProjetoRepository projetoRepository,
                             ProjetoMapper projetoMapper) {
        this.projetoService = projetoService;
        this.projetoRepository = projetoRepository;
        this.projetoMapper = projetoMapper;
    }

    @GetMapping
    @CheckSecurity.comum.all
    public ResponseEntity<List<ProjetoDTO>> get(FiltroProjeto filtros,
                                                @RequestParam Integer page, @RequestParam Integer size) {
        log.info("LISTAGEM DE PROJETOS");
        List<ProjetoDTO> projetosDTO = List.of();

        log.info("Contando projetos por filtros. FILTROS: {}.", filtros);
        final Long total = projetoRepository.contaPorFiltros(filtros);

        if(total > 0) {
            log.info("Pesquisando projetos.");
            final List<Projeto> projetos = projetoRepository
                    .pesquisaPorFiltros(filtros, PageRequest.of(page, size));

            log.info("Convertendo entidades de Projeto em DTO. PROJETOS: {}.", projetos);
            projetosDTO = projetoMapper.toResourceList(projetos);
        }

        log.info("Retornando dtos de resposta. DTOS: {}.", projetosDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(WebUtil.X_TOTAL_COUNT_HEADER, String.valueOf(total))
                .body(projetosDTO);
    }

    @GetMapping("/{id}")
    @CheckSecurity.comum.all
    public ResponseEntity<ProjetoDTO> getById(@PathVariable Long id) {
        log.info("LISTAGEM DE PROJETO POR ID");

        log.info("Iniciando processo de busca por ID. ID: {}.", id);
        final Projeto projeto = projetoService.pesquisaProjetoPorId(id);

        log.info("Convertendo entidade de Projeto em DTO. PROJETO: {}.", projeto);
        final ProjetoDTO projetoDTO = projetoMapper.toResource(projeto);

        log.info("Retornando resposta da operação.");
        return ResponseEntity.ok(projetoDTO);
    }

    @PostMapping
    @CheckSecurity.comum.maintain
    public ResponseEntity<Void> post(@Valid @RequestBody ProjetoParam projetoParam) {
        log.info("CADASTRO DE PROJETOS");

        log.info("Iniciando processo de cadastramento do projeto. PROJETO: {}.", projetoParam);
        projetoService.cadastraProjeto(projetoParam);

       log.info("Retornando resposta da operação.");
       return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    @CheckSecurity.comum.maintain
    public ResponseEntity<ProjetoDTO> put(@PathVariable Long id, @Valid @RequestBody ProjetoParam projetoParam) {
        log.info("ATUALIZAÇÃO DE PROJETOS");

        log.info("Iniciando processo de atualização do projeto. PROJETO: {}.", projetoParam);
        final Projeto projeto = projetoService.atualizaProjeto(projetoParam, id);

        log.info("Convertendo entidade de Projeto em DTO. PROJETO: {}.", projeto);
        final ProjetoDTO projetoDTO = projetoMapper.toResource(projeto);

        log.info("Retornando resposta da operação.");
        return ResponseEntity.ok(projetoDTO);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity.comum.maintain
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("EXCLUSÃO DE PROJETOS");

        log.info("Iniciando processo de exclusão de projeto.");
        projetoService.removeProjeto(id);

        log.info("Retornando resposta da operação.");
        return ResponseEntity
                .noContent()
                .build();
    }
}