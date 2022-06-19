package servicios;

import modelos.Usuario;

import java.util.ArrayList;

public class UsuarioServicio {
    private static UsuarioServicio instancia;
    private ArrayList<Usuario> usuarios = new ArrayList<>();

    private UsuarioServicio(){

        usuarios.add(new Usuario("admin", "administrador", "admin"));
    }

    public static UsuarioServicio getInstancia(){
        if(instancia == null)
            instancia = new UsuarioServicio();

        return instancia;
    }

    public void setUsuario(Usuario usuario){
        usuarios.add(usuario);
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public Usuario autenticar(String usuario, String password){
        for (Usuario user : usuarios) {
            if(user.getUsuario().equals(usuario) && user.getPassword().equals(password)){
                return user;
            }
        }
        return null;
    }
}
