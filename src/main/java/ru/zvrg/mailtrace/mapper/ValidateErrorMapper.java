package ru.zvrg.mailtrace.mapper;

import jakarta.validation.ConstraintViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.zvrg.mailtrace.common.error.ValidateError;
import ru.zvrg.mailtrace.common.error.ValidateErrorList;

import java.util.stream.Collectors;

@Component
public class ValidateErrorMapper {

    public ValidateErrorList mapValidateErrorList(ConstraintViolationException exception) {
        var exceptions = exception.getConstraintViolations().stream()
                .map(error -> new ValidateError(
                        error.getPropertyPath().toString(),
                        error.getMessage()
                ))
                .collect(Collectors.toList());

        return new ValidateErrorList(exceptions);
    }

    public ValidateErrorList mapValidateErrorList(MethodArgumentNotValidException exception) {
        var exceptions = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new ValidateError(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .collect(Collectors.toList());

        return new ValidateErrorList(exceptions);
    }
}
