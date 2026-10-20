package com.proyecto.reservas.exception;

public class ConflictoReservaException extends RuntimeException {
    public ConflictoReservaException(String mensaje) {
        super(mensaje);
    }
}
