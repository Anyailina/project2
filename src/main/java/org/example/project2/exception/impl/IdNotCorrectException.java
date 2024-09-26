package org.example.project2.exception.impl;

import org.example.project2.enums.ExceptionEnum;
import org.example.project2.exception.InternalException;

public class IdNotCorrectException extends InternalException {
    public IdNotCorrectException() {
        super(ExceptionEnum.ID_NOT_CORRECT);
    }
}
