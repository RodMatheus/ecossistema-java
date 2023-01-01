package br.com.exemplo.comum.api.v1.controller;

import br.com.exemplo.comum.api.v1.filter.FiltroCentroDeCusto;
import br.com.exemplo.comum.api.v1.model.dto.CentroDeCustoDTO;
import br.com.exemplo.comum.api.v1.model.input.CentroDeCustoParam;
import br.com.exemplo.comum.api.v1.openapi.CentroDeCustoControllerOpenApi;
import br.com.exemplo.comum.core.security.CheckSecurity;
import br.com.exemplo.comum.domain.model.entities.CentroDeCusto;
import br.com.exemplo.comum.domain.repository.CentroDeCustoRepository;
import br.com.exemplo.comum.domain.service.CentroDeCustoService;
import br.com.exemplo.comum.api.v1.mapper.CentroDeCustoMapper;
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
@RequestMapping(value = "/centros-de-custo",
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class CentroDeCustoController implements CentroDeCustoControllerOpenApi {

    private final CentroDeCustoService centroDeCustoService;
    private final CentroDeCustoRepository centroDeCustoRepository;
    private final CentroDeCustoMapper centroDeCustoMapper;

    public CentroDeCustoController(CentroDeCustoService centroDeCustoService,
                                   CentroDeCustoRepository centroDeCustoRepository,
                                   CentroDeCustoMapper centroDeCustoMapper) {
        this.centroDeCustoService = centroDeCustoService;
        this.centroDeCustoRepository = centroDeCustoRepository;
        this.centroDeCustoMapper = centroDeCustoMapper;
    }

    @GetMapping
    @CheckSecurity.comum.all
    public ResponseEntity<List<CentroDeCustoDTO>> get(FiltroCentroDeCusto filtros,
                                                      @RequestParam(defaultValue = WebUtil.PAGE_DEFAULT) Integer page,
                                                      @RequestParam(defaultValue = WebUtil.SIZE_DEFAULT) Integer size) {
        log.info("LISTAGEM DE CENTROS DE CUSTO");
        List<CentroDeCustoDTO> centrosDeCustoDTO = List.of();

        log.info("Contando centros de custo por filtros. FILTROS: {}.", filtros);
        final Long total = centroDeCustoRepository.contaPorFiltros(filtros);

        if(total > 0) {
            log.info("Pesquisando centros de custo.");
            final List<CentroDeCusto> centrosDeCustos = centroDeCustoRepository
                    .pesquisaPorFiltros(filtros, PageRequest.of(page, size));

            log.info("Convertendo entidades de Centro de custo em DTO.");
            centrosDeCustoDTO = centroDeCustoMapper.toResourceList(centrosDeCustos);
        }

        log.info("Retornando dtos de resposta. DTOS: {}.", centrosDeCustoDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(WebUtil.X_TOTAL_COUNT_HEADER, String.valueOf(total))
                .body(centrosDeCustoDTO);
    }

    @GetMapping("/{id}")
    @CheckSecurity.comum.all
    public ResponseEntity<CentroDeCustoDTO> getById(@PathVariable Long id) {
        log.info("LISTAGEM DE CENTRO DE CUSTO POR ID");

        log.info("Iniciando processo de busca por ID.");
        final CentroDeCusto centroDeCusto = centroDeCustoService.pesquisaCentroDeCustoPorId(id);

        log.info("Convertendo entidade de Centro de custo em DTO.");
        final CentroDeCustoDTO centroDeCustoDTO = centroDeCustoMapper.toResource(centroDeCusto);

        log.info("Retornando resposta da operação. DTO: {}.", centroDeCustoDTO);
        return ResponseEntity.ok(centroDeCustoDTO);
    }

    @PostMapping
    @CheckSecurity.comum.maintain
    public ResponseEntity<Void> post(@Valid @RequestBody CentroDeCustoParam centroDeCustoParam) {
        log.info("CADASTRO DE CENTROS DE CUSTO");

        log.info("Iniciando processo de cadastramento do centro de custo. CENTRO DE CUSTO: {}.", centroDeCustoParam);
        centroDeCustoService.cadastraCentroDeCusto(centroDeCustoParam);

       log.info("Retornando resposta da operação.");
       return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    @CheckSecurity.comum.maintain
    public ResponseEntity<CentroDeCustoDTO> put(@PathVariable Long id, @Valid @RequestBody CentroDeCustoParam centroDeCustoParam) {
        log.info("ATUALIZAÇÃO DE CENTROS DE CUSTO");

        log.info("Iniciando processo de atualização do centro de custo. CENTRO DE CUSTO: {}.", centroDeCustoParam);
        final CentroDeCusto centroDeCusto = centroDeCustoService.atualizaCentroDeCusto(centroDeCustoParam, id);

        log.info("Convertendo entidade de Centro de custo em DTO.");
        final CentroDeCustoDTO centroDeCustoDTO = centroDeCustoMapper.toResource(centroDeCusto);

        log.info("Retornando resposta da operação. DTO: {}.", centroDeCustoDTO);
        return ResponseEntity.ok(centroDeCustoDTO);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity.comum.maintain
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("EXCLUSÃO DE CENTROS DE CUSTO");

        log.info("Iniciando processo de exclusão de centro de custo.");
        centroDeCustoService.removeCentroDeCusto(id);

        log.info("Retornando resposta da operação.");
        return ResponseEntity
                .noContent()
                .build();
    }
}