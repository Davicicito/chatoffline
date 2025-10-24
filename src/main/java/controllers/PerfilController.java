package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Usuario;
import service.ExportService;
import service.ZipService;
import util.Session;

import java.io.File;
import java.io.IOException;

public class PerfilController {

    @FXML private Label lblNombre;
    @FXML private Label lblEmail;
    @FXML private Label lblStatus;

    private final ExportService exportService = new ExportService();
    private final ZipService zipService = new ZipService();

    /**
     * Se ejecuta al abrir la ventana de perfil del usuario.
     * Muestra el nombre y correo del usuario que ha iniciado sesión.
     */
    @FXML
    public void initialize() {
        Usuario usuarioActual = Session.getCurrentUser();
        if (usuarioActual != null) {
            lblNombre.setText("Nombre: " + usuarioActual.getNombre());
            lblEmail.setText("Email: " + usuarioActual.getEmail());
        }
    }

    /**
     * Crea una copia de seguridad del usuario actual.
     * Primero exporta todos los mensajes a un archivo CSV, y después los comprime junto
     * a los archivos adjuntos en un ZIP dentro de la carpeta "exported".
     * @param event Evento del botón "Hacer copia de seguridad".
     */
    @FXML
    private void hacerCopiaDeSeguridad(ActionEvent event) {
        Usuario usuarioActual = Session.getCurrentUser();
        if (usuarioActual == null) {
            mostrarAlerta("Error", "No se ha podido identificar al usuario.", Alert.AlertType.ERROR);
            return;
        }

        // 1. Crear un archivo CSV temporal
        File archivoCsvTemporal = new File("export_temp.csv");
        boolean exportado = exportService.exportarDatosUsuario(usuarioActual, archivoCsvTemporal);

        if (!exportado) {
            mostrarAlerta("Información", "No tienes mensajes para exportar.", Alert.AlertType.INFORMATION);
            archivoCsvTemporal.delete();
            return;
        }

        // 2. Crear carpeta "exported" si no existe
        File carpetaExported = new File("exported");
        if (!carpetaExported.exists()) {
            carpetaExported.mkdirs();
        }

        // 3. Crear el archivo ZIP dentro de la carpeta "exported"
        File archivoZipDestino = new File(carpetaExported, usuarioActual.getNombre() + "_backup.zip");

        // 4. Crear el ZIP
        boolean exito = zipService.crearBackup(archivoZipDestino, archivoCsvTemporal, "media");

        if (exito) {
            mostrarAlerta("Éxito", "Copia de seguridad creada correctamente en:\n" + archivoZipDestino.getAbsolutePath(), Alert.AlertType.INFORMATION);
        } else {
            mostrarAlerta("Error", "No se pudo crear la copia de seguridad.", Alert.AlertType.ERROR);
        }

        // 5. Borrar archivo temporal CSV
        archivoCsvTemporal.delete();
    }


    /**
     * Permite volver al chat principal cerrando la ventana actual del perfil.
     * @param event Evento del botón "Volver al chat".
     */
    @FXML
    private void volverChat(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/mainchat.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) lblNombre.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cierra la sesión actual y devuelve al usuario a la pantalla de inicio de sesión.
     * @param event Evento del botón "Cerrar sesión".
     */
    @FXML
    private void cerrarSesion(ActionEvent event) {
        Session.clear();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) lblNombre.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Muestra una alerta sencilla en pantalla con el mensaje que se le pasa.
     * Se usa para mostrar errores, avisos o confirmaciones al usuario.
     * @param titulo Título de la ventana de alerta.
     * @param contenido Mensaje principal que se mostrará.
     * @param tipo Tipo de alerta (información, error, etc.).
     */
    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
