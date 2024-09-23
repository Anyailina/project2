package org.example.project2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public class InternalException extends RuntimeException {
    protected final ExceptionEnum exception;


}
