package org.example.project2.exception;

public class PersonNotCorrect extends InternalException {
    public PersonNotCorrect() {
        super(ExceptionEnum.INCORRECT_USER);
    }
}
