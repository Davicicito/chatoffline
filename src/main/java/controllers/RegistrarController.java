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
    // Expresión regular para validar emails
    private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
    );

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

            // --- VALIDACIONES CON EXCEPCIONES ---
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
            // Capturar nuestras excepciones personalizadas y mostrar el mensaje de error
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Error: " + e.getMessage());
        }
    }

    /**
     * Valida que el formato del email sea correcto usando una expresión regular.
     * @throws InvalidEmailFormatException si el email no cumple con el formato estándar.
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
