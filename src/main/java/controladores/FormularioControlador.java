package controladores;

import io.javalin.Javalin;
import modelos.Formulario;
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
                    ctx.render("/publico/listado_servidor.html",model);
                });
                get("/mapa",ctx->{
                    String id = ctx.queryParam("id");
                    HashMap<String,Object> model = new HashMap<String, Object>();
                    Formulario formulario = formularioServicio.findbyId(id);

                    model.put("latitud",formulario.getUbicacion().getLatitud());
                    model.put("longitud",formulario.getUbicacion().getLongitud());
                    ctx.render("/publico/mapa.html",model);
                });
            });
        });
    }
}