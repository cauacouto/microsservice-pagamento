package br.com.alurafood.pagamentos.infra;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExecptionHandler extends ResponseEntityExceptionHandler {

@ExceptionHandler(RuntimeException.class)
    private ResponseEntity<String> tratarErro500(RuntimeException ex){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    private ResponseEntity<String> tratarErro404(HttpClientErrorException.NotFound ex){
   return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
