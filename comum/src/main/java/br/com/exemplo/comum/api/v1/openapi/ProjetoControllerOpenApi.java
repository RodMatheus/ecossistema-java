package br.com.exemplo.comum.api.v1.openapi;

import br.com.exemplo.comum.api.exceptionhandler.ErroDTO;
import br.com.exemplo.comum.api.v1.filter.FiltroProjeto;
import br.com.exemplo.comum.api.v1.model.dto.ProjetoDTO;
import br.com.exemplo.comum.api.v1.model.input.ProjetoParam;
import br.com.exemplo.comum.infrastructure.util.WebUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Projetos", description = "Gerência de projetos")
public interface ProjetoControllerOpenApi {

    @Operation(summary = "Lista de projetos",
        description = "Endpoint para a listagem de projetos com filtros.",
          responses = {
            @ApiResponse(responseCode = "500", description = WebUtil.INTERNAL_SERVER_DEFAULT_RESPONSE,
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "401", description = WebUtil.UNAUTHORIZED_DEFAULT_RESPONSE,
                content = @Content(schema = @Schema(implementation = ErroDTO.class))),
            @ApiResponse(responseCode = "200",
                    headers = @Header(name = WebUtil.X_TOTAL_COUNT_HEADER,
                        description = "Totalização de todos os registros existentes com os filtros selecionados.",
                        schema = @Schema(implementation = Integer.class)))
    })
    ResponseEntity<List<ProjetoDTO>> get(
            @Parameter(in = ParameterIn.QUERY, description = "Filtros de projeto") FiltroProjeto filtros,
            @Parameter(in = ParameterIn.QUERY, example = "1", description = "Definição do página") Integer page,
            @Parameter(in = ParameterIn.QUERY, example = "10", description = "Definição do tamanho da lista") Integer size);

    @Operation(summary = "Lista projeto",
            description = "Endpoint para a listagem detalhada de um projeto.",
            responses = {
                    @ApiResponse(responseCode = "500", description = WebUtil.INTERNAL_SERVER_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "404", description = WebUtil.NOT_FOUND_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "401", description = WebUtil.UNAUTHORIZED_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "200")
            })
    ResponseEntity<ProjetoDTO> getById(@Parameter(required = true, in = ParameterIn.PATH) Long id);

    @Operation(summary = "Cadastra projetos",
            description = "Endpoint para cadastrar novos projetos.",
            responses = {
                    @ApiResponse(responseCode = "500", description = WebUtil.INTERNAL_SERVER_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "403", description = WebUtil.FORBIDDEN_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "401", description = WebUtil.UNAUTHORIZED_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "400", description = WebUtil.BAD_REQUEST_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "201")
    })
    ResponseEntity<Void> post(@Parameter(required = true) ProjetoParam projetoParam);

    @Operation(summary = "Atualiza projetos",
            description = "Endpoint para atualizar um projeto.",
            responses = {
                    @ApiResponse(responseCode = "500", description = WebUtil.INTERNAL_SERVER_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "404", description = WebUtil.NOT_FOUND_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "403", description = WebUtil.FORBIDDEN_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "401", description = WebUtil.UNAUTHORIZED_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "400", description = WebUtil.BAD_REQUEST_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "200")
            })
    ResponseEntity<ProjetoDTO> put(
            @Parameter(required = true, in = ParameterIn.PATH) Long id,
            @Parameter(required = true) ProjetoParam projetoParam);

    @Operation(summary = "Exclui projetos",
            description = "Endpoint para excluir um projeto.",
            responses = {
                    @ApiResponse(responseCode = "500", description = WebUtil.INTERNAL_SERVER_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "404", description = WebUtil.NOT_FOUND_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "403", description = WebUtil.FORBIDDEN_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "401", description = WebUtil.UNAUTHORIZED_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "204")
    })
    ResponseEntity<Void> delete(@Parameter(required = true, in = ParameterIn.PATH) Long id);
}