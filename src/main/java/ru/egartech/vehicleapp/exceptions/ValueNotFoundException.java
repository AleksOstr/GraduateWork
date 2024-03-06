package ru.egartech.vehicleapp.exceptions;

public class ValueNotFoundException extends RuntimeException{
    public ValueNotFoundException(String message) {
        super(message);
    }
}
