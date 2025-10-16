package service;

import jakarta.xml.bind.*;
import model.ListaUsuarios;
import model.Usuario;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLUsuariosService {

    private final File archivoUsuarios = new File("data/usuarios.xml");

    // --- Cargar usuarios ---
    public List<Usuario> cargarUsuarios() {
        try {
            if (!archivoUsuarios.exists()) return new ArrayList<>();

            JAXBContext context = JAXBContext.newInstance(ListaUsuarios.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ListaUsuarios lista = (ListaUsuarios) unmarshaller.unmarshal(archivoUsuarios);
            return lista.getUsuarios();

        } catch (Exception e) {
            System.out.println("⚠️ Error al cargar usuarios: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // --- Guardar usuarios ---
    private void guardarUsuarios(List<Usuario> usuarios) {
        try {
            // Asegurar que el directorio data/ existe
            File dir = archivoUsuarios.getParentFile();
            if (!dir.exists()) {
                boolean creado = dir.mkdirs();
                System.out.println("Carpeta data creada: " + creado);
            }

            JAXBContext context = JAXBContext.newInstance(ListaUsuarios.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            ListaUsuarios lista = new ListaUsuarios(usuarios);
            marshaller.marshal(lista, archivoUsuarios);

            System.out.println("✅ Usuarios guardados correctamente en " + archivoUsuarios.getAbsolutePath());

        } catch (Exception e) {
            System.out.println("❌ Error al guardar usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // --- Registrar usuario ---
    public boolean registrarUsuario(Usuario nuevoUsuario) {
        List<Usuario> usuarios = cargarUsuarios();

        for (Usuario u : usuarios) {
            if (u.getNombre().equalsIgnoreCase(nuevoUsuario.getNombre())
                    || u.getEmail().equalsIgnoreCase(nuevoUsuario.getEmail())) {
                return false; // Ya existe
            }
        }

        usuarios.add(nuevoUsuario);
        guardarUsuarios(usuarios);
        return true;
    }

    // --- Buscar usuario ---
    public Usuario buscarUsuario(String nombre, String email) {
        return cargarUsuarios().stream()
                .filter(u -> u.getNombre().equalsIgnoreCase(nombre)
                        && u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}
