package br.com.exemplo.comum.api.v1.openapi;

import br.com.exemplo.comum.api.exceptionhandler.ErroDTO;
import br.com.exemplo.comum.api.v1.model.dto.PlanoDeContasDTO;
import br.com.exemplo.comum.api.v1.model.input.PlanoDeContasParam;
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

@Tag(name = "Plano de contas", description = "Gerência de planos de contas")
public interface PlanoDeContasControllerOpenApi {

    @Operation(summary = "Lista de planos de contas",
        description = "Endpoint para a listagem de planos de contas.",
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
    ResponseEntity<List<PlanoDeContasDTO>> get();

    @Operation(summary = "Lista plano de contas",
            description = "Endpoint para a listagem detalhada de um plano de contas.",
            responses = {
                    @ApiResponse(responseCode = "500", description = WebUtil.INTERNAL_SERVER_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "404", description = WebUtil.NOT_FOUND_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "401", description = WebUtil.UNAUTHORIZED_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "200")
            })
    ResponseEntity<PlanoDeContasDTO> getById(@Parameter(required = true, in = ParameterIn.PATH) Long id);

    @Operation(summary = "Cadastra planos de contas",
            description = "Endpoint para cadastrar novos planos de contas.",
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
    ResponseEntity<Void> post(@Parameter(required = true) PlanoDeContasParam planoDeContasParam);

    @Operation(summary = "Atualiza planos de contas",
            description = "Endpoint para atualizar um plano de contas.",
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
    ResponseEntity<PlanoDeContasDTO> put(
            @Parameter(required = true, in = ParameterIn.PATH) Long id,
            @Parameter(required = true) PlanoDeContasParam planoDeContasParam);

    @Operation(summary = "Exclui plano de contas",
            description = "Endpoint para excluir um plano de contas.",
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

    @Operation(summary = "Inativa planos de contas",
            description = "Endpoint para inativar um plano de contas e seus filhos respectivamente.",
            responses = {
                    @ApiResponse(responseCode = "500", description = WebUtil.INTERNAL_SERVER_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "404", description = WebUtil.NOT_FOUND_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "403", description = WebUtil.FORBIDDEN_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "401", description = WebUtil.UNAUTHORIZED_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "200")
            })
    ResponseEntity<PlanoDeContasDTO> inactivate(@Parameter(required = true, in = ParameterIn.PATH) Long id);

    @Operation(summary = "Ativa planos de contas",
            description = "Endpoint para ativar um plano de contas e seus filhos respectivamente.",
            responses = {
                    @ApiResponse(responseCode = "500", description = WebUtil.INTERNAL_SERVER_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "404", description = WebUtil.NOT_FOUND_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "403", description = WebUtil.FORBIDDEN_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "401", description = WebUtil.UNAUTHORIZED_DEFAULT_RESPONSE,
                            content = @Content(schema = @Schema(implementation = ErroDTO.class))),
                    @ApiResponse(responseCode = "200")
            })
    ResponseEntity<PlanoDeContasDTO> activate(@Parameter(required = true, in = ParameterIn.PATH) Long id);
}