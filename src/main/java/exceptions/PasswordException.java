package exceptions;

/**
 * Excepción que se lanza cuando una contraseña no cumple con los requisitos de seguridad.
 */
public class PasswordException extends Exception {
    public PasswordException(String message) {
        super(message);
    }
}
