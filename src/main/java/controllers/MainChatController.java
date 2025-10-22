package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Adjunto;
import model.Mensaje;
import model.Usuario;
import service.AdjuntoService;
import service.XMLMensajesService;
import service.XMLUsuariosService;
import util.Session;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MainChatController {

    @FXML private Label lblUsuario;
    @FXML private ListView<String> listaUsuarios;
    @FXML private TextArea areaChat;
    @FXML private VBox contenedorMensajes;
    @FXML private ScrollPane scrollChat;
    @FXML private TextField txtMensaje;
    @FXML private Label lblAdjunto; // Etiqueta para el nombre del archivo

    private final XMLUsuariosService servicioUsuarios = new XMLUsuariosService();
    private final XMLMensajesService servicioMensajes = new XMLMensajesService();
    private final AdjuntoService servicioAdjuntos = new AdjuntoService(); // Servicio para adjuntos

    private Usuario usuarioActual;
    private String contactoSeleccionado;
    private Adjunto adjuntoActual = null; // Para guardar el adjunto seleccionado

    @FXML
    public void initialize() {
        usuarioActual = Session.getCurrentUser();
        if (usuarioActual == null) {
            lblUsuario.setText("Error de sesión");
            return;
        }
        lblUsuario.setText("Usuario: " + usuarioActual.getNombre());

        List<Usuario> todos = servicioUsuarios.cargarUsuarios();
        List<String> nombres = todos.stream()
                .filter(u -> !u.getNombre().equalsIgnoreCase(usuarioActual.getNombre()))
                .map(Usuario::getNombre)
                .collect(Collectors.toList());
        listaUsuarios.getItems().addAll(nombres);
    }

    @FXML
    private void abrirChat(MouseEvent event) {
        contactoSeleccionado = listaUsuarios.getSelectionModel().getSelectedItem();
        if (contactoSeleccionado == null) return;

        contenedorMensajes.getChildren().clear();
        limpiarAdjunto();

        List<Mensaje> conversacion = servicioMensajes.obtenerConversacion(
                usuarioActual.getNombre(), contactoSeleccionado);

        for (Mensaje msg : conversacion) {
            mostrarMensaje(msg);
        }
    }


    @FXML
    private void adjuntarArchivo(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo para adjuntar");
        File archivoSeleccionado = fileChooser.showOpenDialog(lblUsuario.getScene().getWindow());

        if (archivoSeleccionado != null) {
            // Usar el servicio para guardar el archivo y obtener el objeto Adjunto
            adjuntoActual = servicioAdjuntos.guardarAdjunto(archivoSeleccionado.getAbsolutePath());
            if (adjuntoActual != null) {
                lblAdjunto.setText("Adjunto: " + adjuntoActual.getNombre());
            }
        }
    }

    @FXML
    private void enviarMensaje(ActionEvent event) {
        String contenido = txtMensaje.getText().trim();
        if (contenido.isEmpty() && adjuntoActual == null) return;
        if (contactoSeleccionado == null) return;

        Mensaje nuevoMensaje = new Mensaje(
                usuarioActual.getNombre(), contactoSeleccionado, contenido, adjuntoActual);

        servicioMensajes.enviarMensaje(nuevoMensaje);
        mostrarMensaje(nuevoMensaje);

        txtMensaje.clear();
        limpiarAdjunto();
    }


    private void limpiarAdjunto() {
        adjuntoActual = null;
        lblAdjunto.setText("");
    }

    @FXML
    private void abrirPerfil() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/perfil.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) lblUsuario.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void mostrarMensaje(Mensaje msg) {
        Label lbl = new Label(msg.getContenido());
        lbl.setWrapText(true);
        lbl.setMaxWidth(300);
        lbl.setStyle("-fx-padding: 8; -fx-background-radius: 10; -fx-font-size: 13;");

        HBox burbuja = new HBox(lbl);
        burbuja.setPadding(new javafx.geometry.Insets(5, 10, 5, 10));

        // Mensaje propio → derecha (verde)
        if (msg.getRemitente().equalsIgnoreCase(usuarioActual.getNombre())) {
            burbuja.setStyle("-fx-alignment: center-right;");
            lbl.setStyle(lbl.getStyle() +
                    "-fx-background-color: #DCF8C6; -fx-text-fill: black;");
        }
        // Mensaje recibido → izquierda (gris)
        else {
            burbuja.setStyle("-fx-alignment: center-left;");
            lbl.setStyle(lbl.getStyle() +
                    "-fx-background-color: #EAEAEA; -fx-text-fill: black;");
        }

        contenedorMensajes.getChildren().add(burbuja);

        // Scroll automático hacia abajo
        scrollChat.layout();
        scrollChat.setVvalue(1.0);
    }

}
