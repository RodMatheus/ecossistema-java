package br.com.exemplo.comum.api.v1.openapi;

import br.com.exemplo.comum.api.exceptionhandler.ErroDTO;
import br.com.exemplo.comum.domain.model.Departamento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Departamentos", description = "Gerência de departamentos")
public interface DepartamentoControllerOpenApi {

    @Operation(summary = "Listagem de departamentos",
        description = "Endpoint para a listagem de departamentos por ordem alfabética.",
          responses = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "500", description = "Problema inesperado no sistema.",
                content = @Content(schema = @Schema(implementation = ErroDTO.class)))
    })
    ResponseEntity<Departamento> get();
}