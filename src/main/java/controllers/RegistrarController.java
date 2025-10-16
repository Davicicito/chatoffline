package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Usuario;
import service.XMLUsuariosService;

import java.io.IOException;
public class RegistrarController {
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtContraseña; // Campo añadido
    @FXML private Label lblMensaje;

    private final XMLUsuariosService servicioUsuarios = new XMLUsuariosService();

    @FXML
    private void registrarUsuario(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String email = txtEmail.getText().trim();
        String contraseña = txtContraseña.getText().trim(); // Campo añadido

        if (nombre.isEmpty() || email.isEmpty() || contraseña.isEmpty()) {
            lblMensaje.setText("Rellena todos los campos.");
            return;
        }

        Usuario nuevoUsuario = new Usuario(nombre, email, contraseña);

        if (servicioUsuarios.registrarUsuario(nuevoUsuario)) {
            lblMensaje.setText("Usuario registrado correctamente.");
        } else {
            lblMensaje.setText("Ese usuario o email ya existe.");
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
