package ude.edu.uy.taller2.exception;

/**
 * Excepción lanzada cuando se intenta una operación inválida sobre una venta
 * (por ejemplo, modificar una venta finalizada o introducir cantidades inválidas).
 */
public class InvalidSaleOperationException extends RuntimeException {
    public InvalidSaleOperationException(String message) {
        super(message);
    }
}

