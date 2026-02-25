package ude.edu.uy.taller2.exception;

/**
 * Excepción lanzada cuando se intenta eliminar más unidades de las disponibles.
 * Esta excepción es checked para que pueda propagarse y manejarse explícitamente.
 */
public class InsufficientUnitsException extends Exception {
    public InsufficientUnitsException(String message) {
        super(message);
    }
}
