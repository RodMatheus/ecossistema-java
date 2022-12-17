package br.com.exemplo.comum.api.v1.controller;

import br.com.exemplo.comum.api.v1.openapi.DepartamentoControllerOpenApi;
import br.com.exemplo.comum.domain.model.Departamento;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/departamentos",
        produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class DepartamentoController implements DepartamentoControllerOpenApi {

    @GetMapping
    public ResponseEntity<Departamento> get() {
        return ResponseEntity.ok(null);
    }

    @PostMapping
    public ResponseEntity<Void> post() {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
