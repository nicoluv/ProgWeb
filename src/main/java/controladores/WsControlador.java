package controladores;

import io.javalin.Javalin;
import modelos.Formulario;
import modelos.Ubicacion;
import servicios.FormularioServicio;
import servicios.UbicacionServicio;

import java.util.ArrayList;
import java.util.Arrays;

import static io.javalin.apibuilder.ApiBuilder.path;
import static io.javalin.apibuilder.ApiBuilder.ws;

public class WsControlador {
    private Javalin app;
    private FormularioServicio formServicio = FormularioServicio.getInstancia();
    private UbicacionServicio ubicacionServicio = UbicacionServicio.getInstancia();

    public WsControlador(Javalin app) {
        this.app = app;
    }

    public void aplicarRuta(){
        app.routes(() -> {
            path("/ws",() -> {
                ws("/registrar", ws -> {

                    ws.onConnect(ctx -> {
                        System.out.println("Conexión Iniciada - "+ctx.getSessionId());
                    });

                    ws.onMessage(ctx -> {
                        ctx.headerMap();
                        ctx.pathParamMap();
                        ctx.queryParamMap();
                        String mensaje = ctx.message();

                        ArrayList<String> lista = new ArrayList<>(new ArrayList<>(Arrays.asList(mensaje.split(","))));


                        for(int i = 0;i<lista.size();i+=5){
                            Ubicacion ubicacion = new Ubicacion(Double.parseDouble(lista.get(i+3)),Double.parseDouble(lista.get(i+4)));
                            Formulario formulario = new Formulario(lista.get(i),lista.get(i+1),lista.get(i+2),ubicacion);

                            ubicacionServicio.crear(ubicacion);
                            formServicio.crear(formulario);
                        }

                        ctx.send("LISTO :D");
                    });

                    ws.onClose(ctx -> {
                        System.out.println("Conexión Cerrada - "+ctx.getSessionId());
                    });

                    ws.onError(ctx -> {
                        System.out.println("Ocurrió un error en el WS");
                    });
                });
            });
        });
    }
}
