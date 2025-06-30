
package com.spboot.todo.exception;

import org.springframework.http.HttpStatus;
import lombok.Getter;

@Getter
public class TodoApiException extends RuntimeException {
    private HttpStatus status;

    public TodoApiException(HttpStatus status, String message) {
        super(message);  // This calls RuntimeException(message)
        this.status = status;
    }
}
