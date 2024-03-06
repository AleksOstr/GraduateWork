package ru.egartech.vehicleapp.exceptions;

public class ExistingValueException extends RuntimeException{

    public ExistingValueException(String message) {
        super(message);
    }
}
