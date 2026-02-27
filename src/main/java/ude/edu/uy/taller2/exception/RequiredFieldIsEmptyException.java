package ude.edu.uy.taller2.exception;

public class RequiredFieldIsEmptyException extends Exception {
    public RequiredFieldIsEmptyException(String message) {
        super(message);
    }
}
