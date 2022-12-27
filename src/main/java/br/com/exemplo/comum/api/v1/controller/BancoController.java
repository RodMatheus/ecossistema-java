package br.com.exemplo.comum.api.v1.controller;

import br.com.exemplo.comum.api.v1.filter.FiltroBanco;
import br.com.exemplo.comum.api.v1.mapper.BancoMapper;
import br.com.exemplo.comum.api.v1.model.dto.BancoDTO;
import br.com.exemplo.comum.api.v1.model.input.BancoParam;
import br.com.exemplo.comum.api.v1.openapi.BancoControllerOpenApi;
import br.com.exemplo.comum.core.security.CheckSecurity;
import br.com.exemplo.comum.domain.model.entities.Banco;
import br.com.exemplo.comum.domain.repository.BancoRepository;
import br.com.exemplo.comum.domain.service.BancoService;
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
@RequestMapping(value = "/bancos",
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class BancoController implements BancoControllerOpenApi {

    private final BancoService bancoService;
    private final BancoRepository bancoRepository;
    private final BancoMapper bancoMapper;

    public BancoController(BancoService bancoService,
                           BancoRepository bancoRepository,
                           BancoMapper bancoMapper) {
        this.bancoService = bancoService;
        this.bancoRepository = bancoRepository;
        this.bancoMapper = bancoMapper;
    }

    @GetMapping
    @CheckSecurity.comum.all
    public ResponseEntity<List<BancoDTO>> get(FiltroBanco filtros,
                                              @RequestParam(defaultValue = WebUtil.PAGE_DEFAULT) Integer page,
                                              @RequestParam(defaultValue = WebUtil.SIZE_DEFAULT) Integer size) {
        log.info("LISTAGEM DE BANCOS");
        List<BancoDTO> bancosDTO = List.of();

        log.info("Contando bancos por filtros. FILTROS: {}.", filtros);
        final Long total = bancoRepository.contaPorFiltros(filtros);

        if(total > 0) {
            log.info("Pesquisando bancos.");
            final List<Banco> bancos = bancoRepository
                    .pesquisaPorFiltros(filtros, PageRequest.of(page, size));

            log.info("Convertendo entidades de Banco em DTO. BANCOS: {}.", bancos);
            bancosDTO = bancoMapper.toResourceList(bancos);
        }

        log.info("Retornando dtos de resposta. DTOS: {}.", bancosDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .header(WebUtil.X_TOTAL_COUNT_HEADER, String.valueOf(total))
                .body(bancosDTO);
    }

    @GetMapping("/{id}")
    @CheckSecurity.comum.all
    public ResponseEntity<BancoDTO> getById(@PathVariable Long id) {
        log.info("LISTAGEM DE BANCO POR ID");

        log.info("Iniciando processo de busca por ID. ID: {}.", id);
        final Banco banco = bancoService.pesquisaBancoPorId(id);

        log.info("Convertendo entidade de Banco em DTO. BANCO: {}.", banco);
        final BancoDTO bancoDTO = bancoMapper.toResource(banco);

        log.info("Retornando resposta da operação.");
        return ResponseEntity.ok(bancoDTO);
    }

    @PostMapping
    @CheckSecurity.comum.maintain
    public ResponseEntity<Void> post(@Valid @RequestBody BancoParam bancoParam) {
        log.info("CADASTRO DE BANCOS");

        log.info("Iniciando processo de cadastramento do banco. BANCO: {}.", bancoParam);
        bancoService.cadastraBanco(bancoParam);

       log.info("Retornando resposta da operação.");
       return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @PutMapping("/{id}")
    @CheckSecurity.comum.maintain
    public ResponseEntity<BancoDTO> put(@PathVariable Long id, @Valid @RequestBody BancoParam bancoParam) {
        log.info("ATUALIZAÇÃO DE BANCOS");

        log.info("Iniciando processo de atualização do banco. BANCO: {}.", bancoParam);
        final Banco banco = bancoService.atualizaBanco(bancoParam, id);

        log.info("Convertendo entidade de Banco em DTO. BANCO: {}.", banco);
        final BancoDTO bancoDTO = bancoMapper.toResource(banco);

        log.info("Retornando resposta da operação.");
        return ResponseEntity.ok(bancoDTO);
    }

    @DeleteMapping("/{id}")
    @CheckSecurity.comum.maintain
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("EXCLUSÃO DE BANCOS");

        log.info("Iniciando processo de exclusão de banco.");
        bancoService.removeBanco(id);

        log.info("Retornando resposta da operação.");
        return ResponseEntity
                .noContent()
                .build();
    }
}