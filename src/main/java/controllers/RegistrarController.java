package controllers;

import exceptions.InvalidEmailFormatException;
import exceptions.PasswordException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Usuario;
import service.XMLUsuariosService;

import java.io.IOException;
import java.util.regex.Pattern;

public class RegistrarController {
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtContraseña;
    @FXML private Label lblMensaje;

    private final XMLUsuariosService servicioUsuarios = new XMLUsuariosService();
    // Expresión regular que se usa para comprobar si el email tiene un formato válido
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
    );

    /**
     * Método que se ejecuta cuando el usuario hace clic en el botón "Registrar".
     * Comprueba que todos los campos estén llenos, valida el correo y la contraseña,
     * y si todo está bien, registra al nuevo usuario en el sistema.
     * @param event Evento del botón Registrar.
     */
    @FXML
    private void registrarUsuario(ActionEvent event) {
        try {
            String nombre = txtNombre.getText().trim();
            String email = txtEmail.getText().trim();
            String contraseña = txtContraseña.getText().trim();

            if (nombre.isEmpty() || email.isEmpty() || contraseña.isEmpty()) {
                lblMensaje.setText("Rellena todos los campos.");
                return;
            }

            validarEmail(email);
            validarContrasena(contraseña);

            Usuario nuevoUsuario = new Usuario(nombre, email, contraseña);

            if (servicioUsuarios.registrarUsuario(nuevoUsuario)) {
                lblMensaje.setStyle("-fx-text-fill: green;");
                lblMensaje.setText("Usuario registrado correctamente. Ya puedes iniciar sesión.");
            } else {
                lblMensaje.setStyle("-fx-text-fill: red;");
                lblMensaje.setText("Ese usuario o email ya existe.");
            }

        } catch (InvalidEmailFormatException | PasswordException e) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Error: " + e.getMessage());
        }
    }

    /**
     * Comprueba que el correo electrónico tenga un formato correcto.
     * Si no lo tiene, lanza una excepción para avisar.
     * @param email Correo introducido por el usuario.
     * @throws InvalidEmailFormatException si el formato del correo no es válido.
     */
    private void validarEmail(String email) throws InvalidEmailFormatException {
        if (!EMAIL_REGEX.matcher(email).matches()) {
            throw new InvalidEmailFormatException("El formato del correo electrónico no es válido.");
        }
    }

    /**
     * Valida que la contraseña cumpla con los requisitos mínimos de seguridad.
     * @throws PasswordException si la contraseña no es lo suficientemente fuerte.
     */
    private void validarContrasena(String contrasena) throws PasswordException {
        // Requisitos: al menos 8 caracteres, una mayúscula, una minúscula y un número.
        if (contrasena.length() < 8 ||
            !contrasena.matches(".*[A-Z].*") ||
            !contrasena.matches(".*[a-z].*") ||
            !contrasena.matches(".*[0-9].*")) {
            throw new PasswordException("La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula y un número.");
        }
    }

    /**
     * Permite volver a la pantalla de inicio de sesión desde el registro.
     * Carga la vista del login y la muestra en la ventana actual.
     * @param event Evento del botón o enlace "Volver al login".
     */
    @FXML
    private void volverLogin(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) txtNombre.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            lblMensaje.setText("Error al volver al inicio de sesión.");
        }
    }
}
