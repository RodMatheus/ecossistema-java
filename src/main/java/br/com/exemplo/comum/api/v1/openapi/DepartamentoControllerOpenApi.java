package br.com.exemplo.comum.api.v1.openapi;

import br.com.exemplo.comum.api.exceptionhandler.ErroDTO;
import br.com.exemplo.comum.api.v1.filter.FiltroDepartamento;
import br.com.exemplo.comum.api.v1.model.dto.DepartamentoDTO;
import br.com.exemplo.comum.api.v1.model.input.DepartamentoParam;
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

@Tag(name = "Departamentos", description = "Gerência de departamentos")
public interface DepartamentoControllerOpenApi {

    @Operation(summary = "Lista de departamentos",
        description = "Endpoint para a listagem de departamentos.",
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
    ResponseEntity<List<DepartamentoDTO>> get(
            @Parameter(in = ParameterIn.QUERY, description = "Filtros de departamento") FiltroDepartamento filtros,
            @Parameter(in = ParameterIn.QUERY, example = "1", description = "Definição do página") Integer page,
            @Parameter(in = ParameterIn.QUERY, example = "10", description = "Definição do tamanho da lista") Integer size);

    @Operation(summary = "Cadastra departamentos",
            description = "Endpoint para cadastrar novos de departamentos.",
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
    ResponseEntity<Void> post(@Parameter(required = true) DepartamentoParam departamentoParam);

    @Operation(summary = "Exclui departamentos",
            description = "Endpoint para excluir departamentos.",
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
    ResponseEntity<Void> DELETE(@Parameter(required = true, in = ParameterIn.PATH) Long id);
}