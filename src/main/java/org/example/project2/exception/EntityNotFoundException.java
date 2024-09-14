package org.example.project2.exception;

public class EntityNotFoundException extends Exception{

    public EntityNotFoundException(){
        super("Entity not found");
    }
}
