package ude.edu.uy.taller2.exception;

/**
 * Excepción lanzada cuando se intenta exceder el número máximo de unidades
 * permitidas en una venta.
 * Esta excepción es checked para que pueda propagarse y manejarse explícitamente.
 */
public class MaxUnitsExceededException extends Exception {
    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param message Mensaje de la excepción.
     */
    public MaxUnitsExceededException(String message) {
        super(message);
    }
}
