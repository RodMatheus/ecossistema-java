package br.com.exemplo.comum.api.exceptionhandler;

import br.com.exemplo.comum.core.exception.*;
import br.com.exemplo.comum.infrastructure.util.MensagemUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    private final MensagemUtil mensagemUtil;

    public ApiExceptionHandler(MensagemUtil mensagemUtil) {
        this.mensagemUtil = mensagemUtil;
    }

    @ExceptionHandler(Throwable.class)
    public final ResponseEntity<ProblemDetail> handleAllExceptions(Throwable ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        final String mensagem = mensagemUtil.mensagemPersonalizada("erro.padrao");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(geraErro(HttpStatus.INTERNAL_SERVER_ERROR, mensagem));
    }

    @ExceptionHandler(AplicacaoException.class)
    public final ResponseEntity<ProblemDetail> handleAplicacaoExceptions(AplicacaoException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        final String mensagem = mensagemUtil.mensagemPersonalizada("erro.padrao");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(geraErro(HttpStatus.INTERNAL_SERVER_ERROR, mensagem));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public final ResponseEntity<ProblemDetail> handleAccessDeniedExceptions(
            AccessDeniedException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        final String mensagem = mensagemUtil.mensagemPersonalizada("erro.autorizacao");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(geraErro(HttpStatus.UNAUTHORIZED, mensagem));
    }

    @ExceptionHandler(AcessoNaoAutorizadoException.class)
    public final ResponseEntity<ProblemDetail> handleAcessoNaoAutorizadoExceptions(
            AcessoNaoAutorizadoException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);

        final String mensagem = mensagemUtil.mensagemPersonalizada("erro.autorizacao");
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(geraErro(HttpStatus.UNAUTHORIZED, mensagem));
    }

    @ExceptionHandler(AcessoNegadoException.class)
    public final ResponseEntity<ProblemDetail> handleAcessoNegadoExceptionExceptions(
            AcessoNegadoException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(geraErro(HttpStatus.FORBIDDEN, ex.getMessage()));
    }
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public final ResponseEntity<ProblemDetail> handleRecursoNaoEncontradoExceptions(
            RecursoNaoEncontradoException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(geraErro(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(ValidacaoException.class)
    public final ResponseEntity<ProblemDetail> handleValidacaoExceptions(
            ValidacaoException ex, WebRequest request) {
        log.info(ex.getMessage(), ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(geraErro(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    private ProblemDetail geraErro(final HttpStatus status, final String mensagem) {
        return ProblemDetail.forStatusAndDetail(status, mensagem);
    }
}