package modelos;


import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Usuario {
    @Id
    @Column(nullable = false)
    private String usuario;
    @Column(nullable = false)
    private String password;

    public Usuario(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }



    public Usuario() {
    }


    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
