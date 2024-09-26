package org.example.project2.exception.impl;


import org.example.project2.enums.ExceptionEnum;
import org.example.project2.exception.InternalException;

public class EntityNotFoundException extends InternalException {

    public EntityNotFoundException() {
        super(ExceptionEnum.USER_NOT_FOUND);
    }
}
