package ude.edu.uy.taller2.exception;

/**
 * Excepción lanzada cuando un postre no existe en un contexto donde se esperaba.
 */
public class DessertNotFoundException extends RuntimeException {
    public DessertNotFoundException(String message) {
        super(message);
    }
}

