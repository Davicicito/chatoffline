package exceptions;

/**
 * Excepción que se lanza cuando el formato de un correo electrónico no es válido.
 */
public class InvalidEmailFormatException extends Exception {
    public InvalidEmailFormatException(String message) {
        super(message);
    }
}
