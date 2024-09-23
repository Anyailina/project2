package org.example.project2.exception;


public class EntityNotFoundException extends InternalException {

    public EntityNotFoundException(){
        super(ExceptionEnum.USER_NOT_FOUND);
    }
}
