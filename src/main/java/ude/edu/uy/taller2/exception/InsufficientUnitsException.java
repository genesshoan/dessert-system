package ude.edu.uy.taller2.exception;

/**
 * Excepción lanzada cuando se intenta eliminar más unidades de las disponibles.
 */
public class InsufficientUnitsException extends RuntimeException {
    public InsufficientUnitsException(String message) {
        super(message);
    }
}

