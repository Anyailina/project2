package org.example.project2.configuration;

import org.example.project2.configuration.property.ExceptionDetails;
import org.example.project2.enums.ExceptionEnum;
import org.example.project2.exception.InternalException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Component
@ControllerAdvice
public class ControllerAdviceConfiguration {
    private final ExceptionDetails exceptionDetails;

    public ControllerAdviceConfiguration(ExceptionDetails exceptionDetails) {
        this.exceptionDetails = exceptionDetails;
    }

    @ResponseBody
    @ExceptionHandler({InternalException.class})
    public ResponseEntity<?> handleEntityNotFoundException(InternalException exception) {
        var mapError = exceptionDetails.getErrorDetails();
        var exceptionProperties = Optional.ofNullable(mapError.get(exception.getException()))
                .orElseGet(() -> mapError.get(ExceptionEnum.DEFAULT));

        return new ResponseEntity<>(exceptionProperties, HttpStatusCode.valueOf(exceptionProperties.getHttpCode()));
    }

    @ResponseBody
    @ExceptionHandler({RuntimeException.class})
    public ResponseEntity<?> handleRuntimeException(RuntimeException exception) {
        var mapError = exceptionDetails.getErrorDetails();
        var exceptionProperties = mapError.get(ExceptionEnum.DEFAULT);

        return new ResponseEntity<>(exceptionProperties, HttpStatusCode.valueOf(exceptionProperties.getHttpCode()));
    }
}
