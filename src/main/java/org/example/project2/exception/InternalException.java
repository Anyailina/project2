package org.example.project2.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.project2.enums.ExceptionEnum;

@AllArgsConstructor
@Getter
public abstract class InternalException extends RuntimeException {
    protected final ExceptionEnum exception;
}
