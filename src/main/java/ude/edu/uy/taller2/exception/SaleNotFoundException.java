package ude.edu.uy.taller2.exception;

/**
 * Excepción lanzada cuando no se encuentra una venta con el identificador dado.
 */
public class SaleNotFoundException extends RuntimeException {
    public SaleNotFoundException(String message) {
        super(message);
    }
}

