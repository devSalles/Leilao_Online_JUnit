package LeilaoOnlineJUnit.infra.core;

import LeilaoOnlineJUnit.infra.exception.IdNaoEncontradoException;
import LeilaoOnlineJUnit.infra.exception.NenhumRegistroException;
import LeilaoOnlineJUnit.infra.exception.item.*;
import LeilaoOnlineJUnit.infra.exception.leilao.*;
import LeilaoOnlineJUnit.infra.exception.participante.UsuarioBloqueadoException;
import LeilaoOnlineJUnit.infra.exception.participante.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class HandlerException {

    //Exceções globais

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<MessageRestError> ExcecoesGlobais()
//    {
//        MessageRestError messageRestError = new MessageRestError(HttpStatus.INTERNAL_SERVER_ERROR,"Erro interno, tente novamente mais tarde");
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageRestError);
//    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageRestError> errosValidacaoEntrada(MethodArgumentNotValidException ex)
    {
        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error->errors.put(error.getField(),error.getDefaultMessage()));

        MessageRestError messageRestError = new MessageRestError(HttpStatus.BAD_REQUEST,"Erro, verifique os dados",errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageRestError);
    }

    @ExceptionHandler(IdNaoEncontradoException.class)
    public ResponseEntity<MessageRestError> IdNaoEncontradoException(IdNaoEncontradoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.NOT_FOUND,ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageRestError);
    }

    @ExceptionHandler(NenhumRegistroException.class)
    public ResponseEntity<MessageRestError> NenhumRegistroException(NenhumRegistroException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.NOT_FOUND,ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageRestError);
    }

    // --- Exceções Usuário ---

    @ExceptionHandler({
            EmailRepetidoException.class,
            CpfRepetidoException.class
    })
    public ResponseEntity<MessageRestError> excecoesCadastrosRepetidos(Exception ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageRestError);

    }

    @ExceptionHandler({
            CpfNaoEncontradoException.class,
            EmailNaoEncontradoException.class
    })
    public ResponseEntity<MessageRestError> excecoesCpfEEmailNaoEncontrado(Exception ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(messageRestError);
    }

    @ExceptionHandler(PossuiItemEmLeilaoException.class)
    public ResponseEntity<MessageRestError> PossuiItemEmLeilaoException(PossuiItemEmLeilaoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageRestError);
    }

    @ExceptionHandler(PossuiLeilaoAtivoException.class)
    public ResponseEntity<MessageRestError> PossuiLeilaoAtivoException(PossuiLeilaoAtivoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageRestError);
    }

    @ExceptionHandler(UsuarioBloqueadoException.class)
    public ResponseEntity<MessageRestError> UsuarioBloqueadoException(UsuarioBloqueadoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.CONFLICT,ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageRestError);
    }

    @ExceptionHandler(UsuarioAtivoException.class)
    public ResponseEntity<MessageRestError> UsuarioAtivoException(UsuarioAtivoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.CONFLICT,ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageRestError);
    }

    // --- Exceções Item ---

    @ExceptionHandler(ItemComProprietarioVinculadoException.class)
    public ResponseEntity<MessageRestError> ItemComProprietarioVinculadoException(UsuarioBloqueadoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.CONFLICT,ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageRestError);
    }

    @ExceptionHandler(ItemVinculadoAoLeilaoException.class)
    public ResponseEntity<MessageRestError> ItemVinculadoAoLeilaoException(ItemVinculadoAoLeilaoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.CONFLICT,ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageRestError);
    }

    @ExceptionHandler(ItemEmLeilaoException.class)
    public ResponseEntity<MessageRestError> ItemEmLeilaoException(ItemEmLeilaoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageRestError);
    }

    @ExceptionHandler(ItemVendidoException.class)
    public ResponseEntity<MessageRestError> ItemVendidoException(ItemVendidoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageRestError);
    }

    @ExceptionHandler(PrimeiroLanceInvaidoException.class)
    public ResponseEntity<MessageRestError> PrimeiroLanceInvaidoException(PrimeiroLanceInvaidoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.CONFLICT,ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageRestError);
    }

    @ExceptionHandler(LanceInvalidoException.class)
    public ResponseEntity<MessageRestError> LanceInvalidoException(LanceInvalidoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.CONFLICT,ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageRestError);
    }

    @ExceptionHandler(LeilaoNaoAbertoException.class)
    public ResponseEntity<MessageRestError> LeilaoNaoAbertoException(LeilaoNaoAbertoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.CONFLICT,ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageRestError);
    }


    // --- Exceções Leilão ---

    @ExceptionHandler(DataIncorretaException.class)
    public ResponseEntity<MessageRestError> DataIncorretaException(DataIncorretaException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageRestError);
    }

    @ExceptionHandler(StatusDeLeilaoIncorretoException.class)
    public ResponseEntity<MessageRestError> StatusDeLeilaoIncorretoException(StatusDeLeilaoIncorretoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.CONFLICT,ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageRestError);
    }

    @ExceptionHandler(DataInicioLeilaoException.class)
    public ResponseEntity<MessageRestError> DataInicioLeilaoException(DataInicioLeilaoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.BAD_REQUEST,ex.getMessage());
        return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageRestError);
    }

    @ExceptionHandler(PossuiLanceVinculadoException.class)
    public ResponseEntity<MessageRestError> PossuiLanceVinculadoException(PossuiLanceVinculadoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.CONFLICT,ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(messageRestError);
    }

    @ExceptionHandler(LeilaoAbertoException.class)
    public ResponseEntity<MessageRestError> LeilaoAbertoException(LeilaoAbertoException ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.CONFLICT,ex.getMessage());
        return  ResponseEntity.status(HttpStatus.CONFLICT).body(messageRestError);
    }
}
