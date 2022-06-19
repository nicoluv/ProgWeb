package principal;

import controladores.ProductoControlador;
import controladores.UsuarioControlador;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main {
    public static void main(String[] args) {

        Javalin app = Javalin.create(config ->{
            config.addStaticFiles(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                 staticFileConfig.location = Location.CLASSPATH;
            });
            config.enableCorsForAllOrigins();

        }).start(7777);

        new ProductoControlador(app).aplicarRutas();
        new UsuarioControlador(app).aplicarRutas();
    }
}
