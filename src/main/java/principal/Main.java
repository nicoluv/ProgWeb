package principal;

import controladores.ProductoControlador;
import controladores.UsuarioControlador;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import servicios.DatabaseStarter;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {

        DatabaseStarter.startDatabase();

        Javalin app = Javalin.create(config ->{
            config.addStaticFiles(staticFileConfig -> {
                staticFileConfig.hostedPath = "/";
                staticFileConfig.directory = "/publico";
                 staticFileConfig.location = Location.CLASSPATH;
            });
            config.enableCorsForAllOrigins();

        }).start(7000);

        new ProductoControlador(app).aplicarRutas();
        new UsuarioControlador(app).aplicarRutas();
    }
}
