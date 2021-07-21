package project.challengers.exception;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = { ChallengersException.class })
    protected ResponseEntity<ErrorResponse> handleCustomException(ChallengersException e) {
        log.error("handleChallengersException throw ChallengersException : {}", e.getMsgKey());
        return ErrorResponse.toResponseEntity(e);
    }
}
