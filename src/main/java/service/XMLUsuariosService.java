package service;

import jakarta.xml.bind.*;
import model.ListaUsuarios;
import model.Usuario;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XMLUsuariosService {

    private final File archivoUsuarios = new File("data/usuarios.xml");

     /**
     * Carga todos los usuarios registrados desde el archivo XML.
     * Si el archivo no existe, devuelve una lista vacía.
     * @return Lista de objetos Usuario cargados desde usuarios.xml.
     */
    public List<Usuario> cargarUsuarios() {
        try {
            if (!archivoUsuarios.exists()){
                return new ArrayList<>();
            }

            JAXBContext context = JAXBContext.newInstance(ListaUsuarios.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ListaUsuarios lista = (ListaUsuarios) unmarshaller.unmarshal(archivoUsuarios);
            return lista.getUsuarios();

        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * Guarda una lista de usuarios en el archivo XML.
     * Si la carpeta "data" no existe, se crea automáticamente.
     * @param usuarios Lista de usuarios que se quiere guardar.
     */
    private void guardarUsuarios(List<Usuario> usuarios) {
        try {
            // Asegurar que el directorio data/ existe
            File dir = archivoUsuarios.getParentFile();
            if (!dir.exists()) {
                 dir.mkdirs();
                }
            JAXBContext context = JAXBContext.newInstance(ListaUsuarios.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.marshal(new ListaUsuarios(usuarios), archivoUsuarios);
        } catch (Exception e) {
            System.out.println("Error al guardar usuarios: " + e.getMessage());
        }
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Comprueba que no haya otro usuario con el mismo nombre o email antes de guardarlo.
     * @param nuevoUsuario Usuario que se quiere registrar.
     * @return true si el usuario se registró correctamente, false si ya existe.
     */
    public boolean registrarUsuario(Usuario nuevoUsuario) {
        List<Usuario> usuarios = cargarUsuarios();

        for (Usuario u : usuarios) {
            if (u.getNombre().equalsIgnoreCase(nuevoUsuario.getNombre()) || u.getEmail().equalsIgnoreCase(nuevoUsuario.getEmail())) {
            return false;
            }
        }

        usuarios.add(nuevoUsuario);
        guardarUsuarios(usuarios);
        return true;
    }

    /**
     * Busca un usuario por nombre y correo electrónico.
     * Si lo encuentra, lo devuelve; si no, devuelve null.
     * @param nombre Nombre del usuario.
     * @param email Correo del usuario.
     * @return El usuario encontrado o null si no existe.
     */
    public Usuario buscarUsuario(String nombre, String email) {
        return cargarUsuarios().stream()
                .filter(u -> u.getNombre().equalsIgnoreCase(nombre) && u.getEmail().equalsIgnoreCase(email))
                .findFirst()
                .orElse(null);
    }
}
