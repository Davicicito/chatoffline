package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import util.Session;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Usuario;
import service.XMLUsuariosService;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtContraseña; // Campo añadido
    @FXML private Label lblMensaje;

    private final XMLUsuariosService servicioUsuarios = new XMLUsuariosService();

    /**
     * Se ejecuta cuando le doy al botón de 'Iniciar sesión'.
     * Coge los datos de los campos, comprueba que no estén vacíos y busca si el usuario existe.
     * Si el usuario y la contraseña son correctos, lo guarda en la sesión y abre la pantalla del chat.
     * Si algo falla, muestra un mensaje de error.
     * @param event El evento que se produce al hacer clic en el botón.
     */
    @FXML
    private void iniciarSesion(ActionEvent event) {
        String nombre = txtUsuario.getText().trim();
        String email = txtEmail.getText().trim();
        String contraseña = txtContraseña.getText().trim();

        if (nombre.isEmpty() || email.isEmpty() || contraseña.isEmpty()) {
            lblMensaje.setText("Rellena todos los campos.");
            return;
        }

        Usuario usuario = servicioUsuarios.buscarUsuario(nombre, email);
        if (usuario == null || !usuario.getContraseña().equals(contraseña)) {
            lblMensaje.setText("Usuario, email o contraseña incorrectos.");
        return;
        }

        Session.setCurrentUser(usuario);
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainchat.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Chat - " + usuario.getNombre());

        } catch (IOException e) {
            lblMensaje.setText("Error al abrir la pantalla principal.");
            e.printStackTrace();
        }
    }


    /**
     * Se ejecuta cuando hago clic en el enlace '¿No tienes cuenta? Regístrate'.
     * Carga la pantalla de registro (registrar.fxml) y la muestra en la ventana actual.
     * @param event El evento que se produce al hacer clic en el enlace.
     */
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
