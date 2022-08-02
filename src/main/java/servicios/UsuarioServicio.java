package servicios;

import modelos.Formulario;
import modelos.Usuario;

public class UsuarioServicio extends DBManager<Usuario>{
    private static UsuarioServicio instancia;

    public static UsuarioServicio getInstancia(){
        if(instancia == null){
            instancia = new UsuarioServicio();
        }
        return instancia;
    }

    private UsuarioServicio(){super(Usuario.class);
    }

    public boolean autenticar(String usuario, String password) {
        Usuario user = instancia.findbyId(usuario);

        if(user != null && user.getPassword().equals(password)) {
                return true;
        }

        return false;
    }
}
