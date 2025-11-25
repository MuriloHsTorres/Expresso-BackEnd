package com.expresso.finance.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Esta anotação faz o Spring retornar um 404 NOT FOUND automaticamente
// sempre que esta exceção for lançada
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    // Construtor
    public ResourceNotFoundException(String message) {
        super(message);
    }
}