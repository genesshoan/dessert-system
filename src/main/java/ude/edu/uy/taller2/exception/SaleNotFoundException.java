package ude.edu.uy.taller2.exception;

/**
 * Excepción lanzada cuando no se encuentra una venta con el identificador dado.
 * Esta excepción es checked para que pueda propagarse y manejarse explícitamente.
 */
public class SaleNotFoundException extends Exception {
    public SaleNotFoundException(String message) {
        super(message);
    }
}
