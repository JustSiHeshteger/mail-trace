package ru.zvrg.mailtrace.controller;

import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.zvrg.mailtrace.common.error.ValidateErrorList;
import ru.zvrg.mailtrace.common.exception.MailTraceException;
import ru.zvrg.mailtrace.mapper.ValidateErrorMapper;

/**
 * Контроллер для перехвата ошибок
 */
@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorHandlingControllerAdvice {

    private final ValidateErrorMapper validateErrorMapper;

    /**
     * Отлавливает ошибки ограничений
     * @param exception исключение при валидации
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus
    public ResponseEntity<ValidateErrorList> onConstraintValidationException(ConstraintViolationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(validateErrorMapper.mapValidateErrorList(exception));
    }

    /**
     * Отлавливает ошибки валидации
     * @param exception исключение при валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidateErrorList> onArgumentNotValidException(MethodArgumentNotValidException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(validateErrorMapper.mapValidateErrorList(exception));
    }

    /**
     * Отлавливает ошибки логики
     * @param exception исключение при выполнении бизнес логики
     */
    @ExceptionHandler(MailTraceException.class)
    public ResponseEntity<String> onApplicationException(MailTraceException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(exception.getMessage());
    }

    /**
     * Отлавливает все прочие ошибки в приложении
     * @param exception любое исключение в приложении
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> onAnyException(RuntimeException exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    }
}
