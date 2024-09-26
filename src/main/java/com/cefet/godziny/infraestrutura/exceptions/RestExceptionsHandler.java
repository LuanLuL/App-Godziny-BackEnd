package com.cefet.godziny.infraestrutura.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.cefet.godziny.infraestrutura.exceptions.atividade.AtividadeNaoEncontradaException;
import com.cefet.godziny.infraestrutura.exceptions.atividade.AtualizarAtividadeStatusErradoException;
import com.cefet.godziny.infraestrutura.exceptions.atividade.CriarAtividadeIncompletaException;
import com.cefet.godziny.infraestrutura.exceptions.atividade.LimiteCargaHorariaExcedidoException;
import com.cefet.godziny.infraestrutura.exceptions.atividade.RemoverAtividadeSimulandoOuAprovadaException;
import com.cefet.godziny.infraestrutura.exceptions.atividade.arquivo.ArquivoInvalidoException;
import com.cefet.godziny.infraestrutura.exceptions.atividade.arquivo.ArquivoNaoEncontradoException;
import com.cefet.godziny.infraestrutura.exceptions.auth.AuthEmailOuSenhaIncorretoException;
import com.cefet.godziny.infraestrutura.exceptions.categoria.CategoriaNaoEncontradaException;
import com.cefet.godziny.infraestrutura.exceptions.categoria.CriarCategoriaIncompletaException;
import com.cefet.godziny.infraestrutura.exceptions.categoria.RemoverCategoriaComAtividadesException;
import com.cefet.godziny.infraestrutura.exceptions.curso.CriarCursoComUsuarioNormalException;
import com.cefet.godziny.infraestrutura.exceptions.curso.CriarCursoIncompletoException;
import com.cefet.godziny.infraestrutura.exceptions.curso.CursoNaoEncontradoException;
import com.cefet.godziny.infraestrutura.exceptions.curso.RemoverCursoComCategoriasException;
import com.cefet.godziny.infraestrutura.exceptions.curso.RemoverCursoComUsuariosException;
import com.cefet.godziny.infraestrutura.exceptions.usuario.CriarUsuarioAdmComCursoException;
import com.cefet.godziny.infraestrutura.exceptions.usuario.CriarUsuarioEmailRepetidoException;
import com.cefet.godziny.infraestrutura.exceptions.usuario.CriarUsuarioIncompletoException;
import com.cefet.godziny.infraestrutura.exceptions.usuario.CriarUsuarioNormalSemCursoException;
import com.cefet.godziny.infraestrutura.exceptions.usuario.RemoverUsuarioComAtividadesException;
import com.cefet.godziny.infraestrutura.exceptions.usuario.UsuarioNaoEncontradoException;

@ControllerAdvice
public class RestExceptionsHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CriarCursoIncompletoException.class)
    public ResponseEntity<RestDefaultErrorMessage> criarCursoInclompletoException(CriarCursoIncompletoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        error.getStatus();
        error.getDetail();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CursoNaoEncontradoException.class)
    public ResponseEntity<RestDefaultErrorMessage> cursoNaoEncontradoException(CursoNaoEncontradoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage();
        error.setStatus(HttpStatus.NOT_FOUND);
        error.setDetail(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CriarCursoComUsuarioNormalException.class)
    public ResponseEntity<RestDefaultErrorMessage> criarCursoComUsuarioNormalException(CriarCursoComUsuarioNormalException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RemoverCursoComUsuariosException.class)
    public ResponseEntity<RestDefaultErrorMessage> removerCursoComUsuarioException(RemoverCursoComUsuariosException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.CONFLICT, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(RemoverCursoComCategoriasException.class)
    public ResponseEntity<RestDefaultErrorMessage> removerCursoComCategoriaException(RemoverCursoComCategoriasException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.CONFLICT, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(CriarUsuarioIncompletoException.class)
    public ResponseEntity<RestDefaultErrorMessage> criarUsuarioIncompletoException(CriarUsuarioIncompletoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CriarUsuarioNormalSemCursoException.class)
    public ResponseEntity<RestDefaultErrorMessage> criarUsuarioNormalSemCursoException(CriarUsuarioNormalSemCursoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CriarUsuarioAdmComCursoException.class)
    public ResponseEntity<RestDefaultErrorMessage> criarUsuarioAdmComCursoException(CriarUsuarioAdmComCursoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CriarUsuarioEmailRepetidoException.class)
    public ResponseEntity<RestDefaultErrorMessage> criarUsuarioEmailRepetidoException(CriarUsuarioEmailRepetidoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.CONFLICT, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ResponseEntity<RestDefaultErrorMessage> usuarioNaoEncontradoException(UsuarioNaoEncontradoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RemoverUsuarioComAtividadesException.class)
    public ResponseEntity<RestDefaultErrorMessage> removerUsuarioComAtividadesException(RemoverUsuarioComAtividadesException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.CONFLICT, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(CategoriaNaoEncontradaException.class)
    public ResponseEntity<RestDefaultErrorMessage> categoriaNaoEncontradaException(CategoriaNaoEncontradaException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CriarCategoriaIncompletaException.class)
    public ResponseEntity<RestDefaultErrorMessage> criarCategoriaInconpletaException(CriarCategoriaIncompletaException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(CampoRepetidoNoBancoException.class)
    public ResponseEntity<RestDefaultErrorMessage> campoRepetidoNoBancoException(CampoRepetidoNoBancoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.CONFLICT, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(RemoverCategoriaComAtividadesException.class)
    public ResponseEntity<RestDefaultErrorMessage> removerCategoriaComAtividadesException(RemoverCategoriaComAtividadesException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.CONFLICT, exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(AtividadeNaoEncontradaException.class)
    public ResponseEntity<RestDefaultErrorMessage> atividadeNaoEncontradaException(AtividadeNaoEncontradaException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(CriarAtividadeIncompletaException.class)
    public ResponseEntity<RestDefaultErrorMessage> criarAtividadeIncompletaException(CriarAtividadeIncompletaException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(LimiteCargaHorariaExcedidoException.class)
    public ResponseEntity<RestDefaultErrorMessage> limiteCargaHorariaExcedidoException(LimiteCargaHorariaExcedidoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AtualizarAtividadeStatusErradoException.class)
    public ResponseEntity<RestDefaultErrorMessage> atualizarAtividadeStatusErradoException(AtualizarAtividadeStatusErradoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(RemoverAtividadeSimulandoOuAprovadaException.class)
    public ResponseEntity<RestDefaultErrorMessage> removerAtividadeSimulandoOuAprovadaException(RemoverAtividadeSimulandoOuAprovadaException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(ArquivoNaoEncontradoException.class)
    public ResponseEntity<RestDefaultErrorMessage> arquivoNaoEncontradoException(ArquivoNaoEncontradoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ArquivoInvalidoException.class)
    public ResponseEntity<RestDefaultErrorMessage> arquivoInvalidoException(ArquivoInvalidoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.BAD_REQUEST, exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(AuthEmailOuSenhaIncorretoException.class)
    public ResponseEntity<RestDefaultErrorMessage> authEmailOuSenhaIncorretoException(AuthEmailOuSenhaIncorretoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.UNAUTHORIZED, exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(UsuarioNaoAutorizadoException .class)
    public ResponseEntity<RestDefaultErrorMessage> usuarioNaoAutorizadoException (UsuarioNaoAutorizadoException exception) {
        RestDefaultErrorMessage error = new RestDefaultErrorMessage(HttpStatus.UNAUTHORIZED, exception.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
}
