package controladores;

import io.javalin.Javalin;
import modelos.Usuario;
import servicios.UsuarioServicio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UsuarioControlador {
    private Javalin app;
    private UsuarioServicio usuarioServ = UsuarioServicio.getInstancia();
    private ArrayList<Usuario> usuarios;
    private String param;

    public UsuarioControlador(Javalin app) {
        usuarios = usuarioServ.getUsuarios();
        this.app = app;
        param = "2";
    }

    public void aplicarRutas(){
        app.routes(() -> {
            path("/login/{id}", () -> {

                get("/", ctx -> {
                    param = ctx.pathParam("id");
                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Inicio de Sesion");
                    modelo.put("param", param);
                    ctx.render("/publico/login.html",modelo);
                });

                post("/autenticar", ctx -> {
                    String usuario = ctx.formParam("usuario");
                    String pass = ctx.formParam("pass");
                    String sesion = ctx.formParam("check");


                    Usuario user = usuarioServ.autenticar(usuario, pass);

                    if(sesion != null && sesion.equals("ok")  && user != null){
                        ctx.cookie("usuario", usuario, 604800);
                    }else if(sesion == null && user != null){
                        ctx.cookie("sesion", usuario);
                    }

                    if(param.equals("0")){
                        ctx.redirect("/CRUD");
                    }

                    if(param.equals("1")){
                        ctx.redirect("/ventas");
                    }

                    if(param.equals("2")){
                        ctx.redirect("/");
                    }
                });
            });
        });

        app.get("/cerrar-sesion", ctx -> {
            ctx.removeCookie("usuario");
            ctx.removeCookie("sesion");
            ctx.redirect("/");
        });
    }
}
