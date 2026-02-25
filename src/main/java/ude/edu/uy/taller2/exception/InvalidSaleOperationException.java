package ude.edu.uy.taller2.exception;

/**
 * Excepción lanzada cuando se intenta una operación inválida sobre una venta
 * (por ejemplo, modificar una venta finalizada o introducir cantidades inválidas).
 * Esta excepción es checked para que pueda propagarse y manejarse explícitamente.
 */
public class InvalidSaleOperationException extends Exception {
    public InvalidSaleOperationException(String message) {
        super(message);
    }
}
