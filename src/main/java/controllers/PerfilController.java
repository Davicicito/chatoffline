package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Usuario;
import util.Session;

import java.io.IOException;

public class PerfilController {

    @FXML private Label lblNombre;
    @FXML private Label lblEmail;

    @FXML
    public void initialize() {
        // Cargar los datos del usuario que ha iniciado sesión
        Usuario usuarioActual = Session.getCurrentUser();
        if (usuarioActual != null) {
            lblNombre.setText("Nombre: " + usuarioActual.getNombre());
            lblEmail.setText("Email: " + usuarioActual.getEmail());
        }
    }

    @FXML
    private void volverChat(ActionEvent event) {
        try {
            // Cargar la vista del chat principal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainchat.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) lblNombre.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        // Limpiar la sesión del usuario
        Session.clear();

        try {
            // Cargar la vista de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) lblNombre.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
