package LeilaoOnlineJUnit.infra.core;

import LeilaoOnlineJUnit.infra.exception.CpfRepetidoException;
import LeilaoOnlineJUnit.infra.exception.EmailRepetidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.HashMap;
import java.util.Map;


@ControllerAdvice
public class HandlerException {

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


    //Excecções Usuário
    @ExceptionHandler({
            EmailRepetidoException.class,
            CpfRepetidoException.class
    })
    public ResponseEntity<MessageRestError> excecoesCadastrosRepetidos(Exception ex)
    {
        MessageRestError messageRestError = new MessageRestError(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageRestError);

    }
}
