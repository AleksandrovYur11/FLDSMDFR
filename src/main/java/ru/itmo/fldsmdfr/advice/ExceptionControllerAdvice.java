package ru.itmo.fldsmdfr.advice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import ru.itmo.fldsmdfr.exceptions.InvalidRoleException;
import ru.itmo.fldsmdfr.security.exceptions.TokenRefreshException;

import java.util.Date;

@RestControllerAdvice
public class ExceptionControllerAdvice {
    @ExceptionHandler(value = InvalidRoleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handlerInvalidRoleException(InvalidRoleException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }

    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage handlerTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        return new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
    }

}
