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

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private TextField txtEmail;
    @FXML private Label lblMensaje;

    private final XMLUsuariosService servicioUsuarios = new XMLUsuariosService();

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String nombre = txtUsuario.getText().trim();
        String email = txtEmail.getText().trim();

        if (nombre.isEmpty() || email.isEmpty()) {
            lblMensaje.setText("Rellena todos los campos.");
            return;
        }

        Usuario usuario = servicioUsuarios.buscarUsuario(nombre, email);
        if (usuario == null) {
            lblMensaje.setText("Usuario no encontrado.");
            return;
        }

        // Aquí abrirías la pantalla principal del chat
        lblMensaje.setText("Inicio de sesión correcto.");
    }

    @FXML
    private void abrirRegistro(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/registrar.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            lblMensaje.setText("Error al abrir la pantalla de registro.");
            e.printStackTrace();
        }
    }
}
