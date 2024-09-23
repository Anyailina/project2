package org.example.project2.configuration;

import org.example.project2.exception.ExceptionDetails;
import org.example.project2.exception.ExceptionEnum;
import org.example.project2.exception.InternalException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
@ControllerAdvice
public class ControllerAdviceConfiguration {
    private final ExceptionDetails exceptionDetails;

    public ControllerAdviceConfiguration(ExceptionDetails exceptionDetails) {
        this.exceptionDetails = exceptionDetails;
    }

    @ResponseBody
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleEntityNotFoundException(RuntimeException exception) {
        var mapError = exceptionDetails.getErrorDetails();
        ExceptionDetails.ExceptionProperties exceptionProperties;

        if (exception instanceof InternalException internalException) {
            ExceptionEnum exceptionEnum = internalException.getException();
            exceptionProperties = mapError.get(exceptionEnum);
        } else {
            exceptionProperties = mapError.get(ExceptionEnum.DEFAULT);
        }

        return new ResponseEntity<>(exceptionProperties, HttpStatusCode.valueOf(exceptionProperties.getHttpCode()));
    }
}
