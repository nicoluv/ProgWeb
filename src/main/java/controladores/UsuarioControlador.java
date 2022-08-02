package controladores;

import modelos.Usuario;
import io.javalin.Javalin;
import modelos.Usuario;
import servicios.UsuarioServicio;

import javax.xml.transform.sax.SAXResult;
import java.util.HashMap;
import java.util.Map;

import static io.javalin.apibuilder.ApiBuilder.*;

public class UsuarioControlador {
    private Javalin app;

    private UsuarioServicio usuarioServ = UsuarioServicio.getInstancia();

    public UsuarioControlador(Javalin app) {
        this.app = app;
        usuarioServ.crear(new Usuario("admin","admin"));
    }

    public void aplicarRutas(){

        app.get("/",ctx -> {
            ctx.redirect("/login");
        });

        app.routes(() -> {
            path("/login", () -> {

                get("/", ctx -> {

                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("titulo", "Inicio de Sesion");
                    ctx.render("/publico/login.html",modelo);
                });

                post("/autenticar", ctx -> {
                    String usuario = ctx.formParam("usuario");
                    String pass = ctx.formParam("pass");

                    if(usuarioServ.autenticar(usuario, pass)) {
                        ctx.cookie("usuario", usuario);
                        ctx.redirect("/formulario");
                    }
                    else{
                        ctx.redirect("/login");
                    }
                });
            });
            path("/registrar",() -> {
                get("/",ctx -> {
                    ctx.render("/publico/registro.html");
                });
                post("/confirmar",ctx -> {
                   String usuario = ctx.formParam("usuario");
                   String pass = ctx.formParam("pass");


                   if(usuarioServ.findbyId(usuario) == null) {
                       Usuario u = new Usuario(usuario, pass);
                       usuarioServ.crear(u);
                       ctx.redirect("/login");
                   }
                   else{
                       ctx.redirect("/registrar");
                   }
                });
            });
        });

        app.get("/cerrar-sesion", ctx -> {
            ctx.req.getSession().invalidate();
            ctx.redirect("/");
        });

    }
}