package ru.itmo.fldsmdfr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRoleException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidRoleException(String role, String message) {
        super(String.format("Failed for Role.%s: %s", role, message));
    }
}
