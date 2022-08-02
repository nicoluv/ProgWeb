package controladores;

import io.javalin.Javalin;
import servicios.FormularioServicio;

import java.util.HashMap;

import static io.javalin.apibuilder.ApiBuilder.*;

public class FormularioControlador {
    private Javalin app;
    private FormularioServicio formularioServicio = FormularioServicio.getInstancia();

    public FormularioControlador(Javalin app) {
        this.app = app;
    }

    public void aplicarRuta(){
        app.routes(() -> {
            path("/formulario", () -> {
                get("/", ctx -> {
                    ctx.render("/publico/crear_formulario.html");
                });
                get("/lista",ctx -> {
                   ctx.render("/publico/listado_local.html");
                });
                get("/editar",ctx -> {
                    HashMap<String,Object> model = new HashMap<String, Object>();
                    model.put("id",ctx.queryParam("id"));
                    ctx.render("/publico/editar_formulario.html",model);
                });
            });
            path("/servidor",() -> {
                get("/listado",ctx->{
                    HashMap<String,Object> model = new HashMap<String, Object>();
                    model.put("formularios",formularioServicio.findAll());
                    ctx.render("/publico/listado_servidor.html");
                });
            });
        });
    }
}
