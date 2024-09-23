package org.example.project2.exception;

public class IdNotCorrectException extends InternalException {
    public IdNotCorrectException() {
        super(ExceptionEnum.ID_NOT_CORRECT);
    }
}
