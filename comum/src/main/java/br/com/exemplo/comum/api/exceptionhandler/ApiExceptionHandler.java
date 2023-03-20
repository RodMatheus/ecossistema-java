package br.com.exemplo.comum.api.exceptionhandler;

import br.com.exemplo.comum.core.exception.*;
import br.com.exemplo.comum.infrastructure.util.MensagemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MensagemUtil mensagemUtil;

    public ApiExceptionHandler(MensagemUtil mensagemUtil) {
        this.mensagemUtil = mensagemUtil;
    }


    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErroDTO> handleAllExceptions(Exception ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        final String mensagem = mensagemUtil.mensagemPersonalizada("erro.padrao");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(geraErro(mensagem));
    }
    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<ErroDTO> handleAllExceptions(Throwable ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        final String mensagem = mensagemUtil.mensagemPersonalizada("erro.padrao");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(geraErro(mensagem));
    }

    @ExceptionHandler(AplicacaoException.class)
    public final ResponseEntity<ErroDTO> handleAplicacaoExceptions(AplicacaoException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        final String mensagem = mensagemUtil.mensagemPersonalizada("erro.padrao");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(geraErro(mensagem));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ErroDTO> handleAccessDeniedExceptions(
            AccessDeniedException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        final String mensagem = mensagemUtil.mensagemPersonalizada("erro.autorizacao");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(geraErro(mensagem));
    }

    @ExceptionHandler(AcessoNaoAutorizadoException.class)
    public final ResponseEntity<ErroDTO> handleAcessoNaoAutorizadoExceptions(
            AcessoNaoAutorizadoException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        final String mensagem = mensagemUtil.mensagemPersonalizada("erro.autorizacao");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(geraErro(mensagem));
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public final ResponseEntity<ErroDTO> handleAcessoNegadoExceptionExceptions(
            AcessoNegadoException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(geraErro(ex.getMessage()));
    }
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public final ResponseEntity<ErroDTO> handleRecursoNaoEncontradoExceptions(
            RecursoNaoEncontradoException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(geraErro(ex.getMessage()));
    }

    @ExceptionHandler(ValidacaoException.class)
    public final ResponseEntity<ErroDTO> handleValidacaoExceptions(
            ValidacaoException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(geraErro(ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        log.error("Ocorreram erros de validação: ", ex);

        Set<ErroDTO.ValidacaoDTO> validacoes = ex.getBindingResult().getFieldErrors().stream()
                .map(campo -> {
                    List<String> codigos = List.of(campo.getCodes());
                    String mensagem = codigos.stream()
                            .limit(1)
                            .map(codigo -> mensagemUtil.mensagemPersonalizada(codigo))
                            .collect(Collectors.joining());

                    return ErroDTO.validacaoOf(campo.getField(), mensagem);
                })
                .collect(Collectors.toSet());

        ErroDTO response = ErroDTO.builder().validacoes(validacoes).build();
        return ResponseEntity.status(status).body(response);
    }

    private ErroDTO geraErro(final String mensagem) {
        return ErroDTO.builder().mensagem(mensagem).build();
    }
}