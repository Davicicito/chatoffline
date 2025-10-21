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

    @FXML
    public void initialize() {
        Usuario usuarioActual = Session.getCurrentUser();
        if (usuarioActual != null) {
            lblNombre.setText("Nombre: " + usuarioActual.getNombre());
            lblEmail.setText("Email: " + usuarioActual.getEmail());
        }
    }

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
            archivoCsvTemporal.delete(); // Limpiar por si acaso
            return;
        }

        // 2. Preguntar al usuario dónde guardar el ZIP
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar copia de seguridad");
        fileChooser.setInitialFileName(usuarioActual.getNombre() + "_backup.zip");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos ZIP", "*.zip"));
        File archivoZipDestino = fileChooser.showSaveDialog(lblNombre.getScene().getWindow());

        if (archivoZipDestino != null) {
            // 3. Crear el archivo ZIP
            boolean exito = zipService.crearBackup(archivoZipDestino, archivoCsvTemporal, "media");

            if (exito) {
                mostrarAlerta("Éxito", "Copia de seguridad creada correctamente en: " + archivoZipDestino.getAbsolutePath(), Alert.AlertType.INFORMATION);
            } else {
                mostrarAlerta("Error", "No se pudo crear la copia de seguridad.", Alert.AlertType.ERROR);
            }
        }

        // 4. Borrar el archivo CSV temporal
        archivoCsvTemporal.delete();
    }

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

    private void mostrarAlerta(String titulo, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }
}
