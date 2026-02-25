package ude.edu.uy.taller2.exception;

/**
 * Excepción lanzada cuando un postre no existe en un contexto donde se esperaba.
 * Esta excepción es checked para que pueda propagarse y manejarse explícitamente.
 */
public class DessertNotFoundException extends Exception {
    public DessertNotFoundException(String message) {
        super(message);
    }
}
