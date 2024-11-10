package ru.clevertec.exceptionhandlestarter.advice;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.clevertec.exceptionhandlestarter.exception.CommentNotFoundException;
import ru.clevertec.exceptionhandlestarter.exception.NewsNotFoundException;
import ru.clevertec.exceptionhandlestarter.response.ValidationErrorResponse;
import ru.clevertec.exceptionhandlestarter.response.Violation;

import java.util.List;

@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {

        List<Violation> violations = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidationErrorResponse(violations));
    }

    @ExceptionHandler({NewsNotFoundException.class, CommentNotFoundException.class})
    public ResponseEntity<String> handleNewsNotFoundException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
