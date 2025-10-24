package util;

import model.Usuario;

/**
 * Clase sencilla para manejar la sesión del usuario que ha iniciado sesión.
 * Guarda al usuario actual en una variable estática para poder acceder
 * a su información desde cualquier parte del programa.
 */
public class Session {

    // Usuario actualmente conectado
    private static Usuario currentUser;

    /**
     * Guarda el usuario que ha iniciado sesión.
     * @param usuario El usuario que acaba de entrar al sistema.
     */
    public static void setCurrentUser(Usuario usuario) {
        currentUser = usuario;
    }

    /**
     * Devuelve el usuario que está actualmente logueado.
     * @return El usuario en sesión (puede ser null si nadie ha iniciado sesión).
     */
    public static Usuario getCurrentUser() {
        return currentUser;
    }

    /**
     * Cierra la sesión actual eliminando al usuario guardado.
     * (Por ejemplo, se usa al cerrar sesión desde el perfil).
     */
    public static void clear() {
        currentUser = null;
    }
}

