package servicios;

import modelos.Usuario;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuarioServicio extends DBManager<Usuario>{
    private static UsuarioServicio instancia;
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private StrongPasswordEncryptor pass = new StrongPasswordEncryptor();

    private UsuarioServicio(){
        super(Usuario.class);
        crear(new Usuario("admin","administrador", pass.encryptPassword("admin")));

    }

    public static UsuarioServicio getInstancia(){
        if(instancia == null){
            return new UsuarioServicio();
        }
        return instancia;
    }

    public void setUsuario(Usuario usuario){
        usuarios.add(usuario);
    }

    public ArrayList<Usuario> getUsuarios() {
        String query = "select * from usuario";
        Connection con = null;
        try{
            con = DatabaseServicio.getInstancia().getConexion();
            PreparedStatement preparedStatement = con.prepareStatement(query);

            ResultSet rs = preparedStatement.executeQuery();
            usuarios = new ArrayList<>();
            while (rs.next()){
                usuarios.add(new Usuario(rs.getString("usuario"), rs.getString("nombre"),
                        rs.getString("password")));
            }
        } catch (SQLException ex){
            Logger.getLogger(ProductoServicio.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(ProductoServicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return usuarios;
    }

    public Usuario autenticar(String usuario, String password){
        for (Usuario user : usuarios) {
            if(user.getUsuario().equals(usuario) && pass.checkPassword(password, user.getPassword())){
                return user;
            }
        }
        return null;
    }
}
