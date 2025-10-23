package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    @FXML private VBox contenedorMensajes;
    @FXML private ScrollPane scrollChat;
    @FXML private TextField txtMensaje;
    @FXML private Label lblAdjunto;

    private final XMLUsuariosService servicioUsuarios = new XMLUsuariosService();
    private final XMLMensajesService servicioMensajes = new XMLMensajesService();
    private final AdjuntoService servicioAdjuntos = new AdjuntoService();

    private Usuario usuarioActual;
    private String contactoSeleccionado;
    private Adjunto adjuntoActual = null;

    @FXML
    public void initialize() {
        usuarioActual = Session.getCurrentUser();
        if (usuarioActual == null) {
            lblUsuario.setText("Error de sesión");
            return;
        }

        lblUsuario.setText("Usuario: " + usuarioActual.getNombre());

        List<String> nombres = servicioUsuarios.cargarUsuarios().stream()
                .map(Usuario::getNombre)
                .filter(nombre -> !nombre.equalsIgnoreCase(usuarioActual.getNombre()))
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
            adjuntoActual = servicioAdjuntos.guardarAdjunto(archivoSeleccionado.getAbsolutePath());
            if (adjuntoActual != null) {
                lblAdjunto.setText("Adjunto: " + adjuntoActual.getNombre());
            }
        }
    }

    @FXML
    private void enviarMensaje(ActionEvent event) {
        String contenido = txtMensaje.getText().trim();
        if ((contenido.isEmpty() && adjuntoActual == null) || contactoSeleccionado == null) return;

        Mensaje nuevoMensaje = new Mensaje(usuarioActual.getNombre(), contactoSeleccionado, contenido, adjuntoActual);
        servicioMensajes.enviarMensaje(nuevoMensaje);
        mostrarMensaje(nuevoMensaje);

        txtMensaje.clear();
        limpiarAdjunto();
    }

    private void mostrarMensaje(Mensaje msg) {
        // Contenedor de una burbuja de mensaje
        VBox mensajeBox = new VBox(5);
        Label lbl = new Label(msg.getContenido());
        lbl.setWrapText(true);
        lbl.setMaxWidth(300);

        // Si hay adjunto, mostrarlo
        if (msg.getAdjunto() != null) {
            String tipo = msg.getAdjunto().getTipo().toLowerCase();
            String ruta = msg.getAdjunto().getRuta();

            if (tipo.equalsIgnoreCase("imagen")) {
                try {
                    // 🔹 Cargar directamente desde la URI guardada
                    Image image = new Image(ruta);
                    if (!image.isError()) {
                        ImageView img = new ImageView(image);
                        img.setFitWidth(200);
                        img.setPreserveRatio(true);
                        mensajeBox.getChildren().add(img);
                    } else {
                        System.out.println(" Imagen no encontrada o inválida: " + ruta);
                    }
                } catch (Exception e) {
                    System.out.println("Error al mostrar imagen: " + e.getMessage());
                }

            } else {
                // 🔹 Mostrar enlace clicable para abrir PDF, DOC, etc.
                Hyperlink link = new Hyperlink("📎 " + msg.getAdjunto().getNombre());
                link.setOnAction(ev -> {
                    try {
                        // Convierte la URI "file:/C:/..." a un File real antes de abrirlo
                        File file = new File(new java.net.URI(ruta));
                        if (file.exists()) {
                            java.awt.Desktop.getDesktop().open(file);
                        } else {
                            System.out.println("️ Archivo no encontrado: " + ruta);
                        }
                    } catch (Exception e) {
                        System.out.println("Error al abrir archivo: " + e.getMessage());
                    }
                });
                mensajeBox.getChildren().add(link);
            }

        }

        mensajeBox.getChildren().add(lbl);

        // Crear la burbuja (HBox para alinear izq/der)
        HBox burbuja = new HBox(mensajeBox);
        burbuja.setMaxWidth(Double.MAX_VALUE);

        if (msg.getRemitente().equalsIgnoreCase(usuarioActual.getNombre())) {
            burbuja.setStyle("-fx-alignment: center-right;");
            lbl.setStyle("-fx-background-color: #DCF8C6; -fx-padding: 8; -fx-background-radius: 10;");
        } else {
            burbuja.setStyle("-fx-alignment: center-left;");
            lbl.setStyle("-fx-background-color: #FFFFFF; -fx-padding: 8; -fx-background-radius: 10;");
        }

        contenedorMensajes.getChildren().add(burbuja);

        // Bajar el scroll al último mensaje
        scrollChat.layout();
        scrollChat.setVvalue(1.0);
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
}
