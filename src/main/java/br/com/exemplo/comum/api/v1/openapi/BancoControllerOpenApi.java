package br.com.exemplo.comum.api.v1.openapi;

import br.com.exemplo.comum.api.exceptionhandler.ErroDTO;
import br.com.exemplo.comum.api.v1.filter.FiltroBanco;
import br.com.exemplo.comum.api.v1.model.dto.BancoDTO;
import br.com.exemplo.comum.api.v1.model.input.BancoParam;
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

@Tag(name = "Bancos", description = "Gerência de bancos")
public interface BancoControllerOpenApi {

    @Operation(summary = "Lista de bancos",
        description = "Endpoint para a listagem de bancos com filtros.",
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
    ResponseEntity<List<BancoDTO>> get(
            @Parameter(in = ParameterIn.QUERY, description = "Filtros de banco") FiltroBanco filtros,
            @Parameter(in = ParameterIn.QUERY, example = "1", description = "Definição do página") Integer page,
            @Parameter(in = ParameterIn.QUERY, example = "10", description = "Definição do tamanho da lista") Integer size);

    @Operation(summary = "Lista banco",
            description = "Endpoint para a listagem detalhada de um banco.",
            responses = {
                    @ApiResponse(responseCode = "500", description = WebUtil.INTERNAL_SERVER_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "404", description = WebUtil.NOT_FOUND_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "401", description = WebUtil.UNAUTHORIZED_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "200")
            })
    ResponseEntity<BancoDTO> getById(@Parameter(required = true, in = ParameterIn.PATH) Long id);

    @Operation(summary = "Cadastra bancos",
            description = "Endpoint para cadastrar novos bancos.",
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
    ResponseEntity<Void> post(@Parameter(required = true) BancoParam bancoParam);

    @Operation(summary = "Atualiza bancos",
            description = "Endpoint para atualizar um banco.",
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
    ResponseEntity<BancoDTO> put(
            @Parameter(required = true, in = ParameterIn.PATH) Long id,
            @Parameter(required = true) BancoParam bancoParam);

    @Operation(summary = "Exclui bancos",
            description = "Endpoint para excluir um banco.",
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