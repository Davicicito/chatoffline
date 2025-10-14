package service;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import model.ListaUsuarios;
import model.Usuario;

import java.io.File;

public class XMLUsuariosService {
    private final File archivoUsuarios = new File("data/usuarios.xml");

    public ListaUsuarios cargarUsuarios() {
        try {
            if (!archivoUsuarios.exists()) {
                return new ListaUsuarios();
            }

            JAXBContext context = JAXBContext.newInstance(ListaUsuarios.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            return (ListaUsuarios) unmarshaller.unmarshal(archivoUsuarios);
        } catch (Exception e) {
            System.out.println("Error al cargar usuarios desde XML: " + e.getMessage());
            return new ListaUsuarios();
        }
    }

    public void guardarUsuarios(ListaUsuarios lista){
        try {
            JAXBContext context = JAXBContext.newInstance(ListaUsuarios.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            marshaller.marshal(lista, archivoUsuarios);
        } catch (Exception e) {
            System.out.println("Error al guardar usuarios en XML: " + e.getMessage());
        }
    }
    public boolean registrarUsuario(Usuario nuevo){
        ListaUsuarios lista = cargarUsuarios();
        if(lista.existeUsurio(nuevo.getEmail())){
            System.out.println("El usuario con email " + nuevo.getEmail() + " ya existe.");
            return false;
        }
        lista.addUsuario(nuevo);
        guardarUsuarios(lista);
        System.out.println("Usuario registrado: " + nuevo);
        return true;
    }

}
