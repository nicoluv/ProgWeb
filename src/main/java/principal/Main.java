package principal;
import controladores.FormularioControlador;
import controladores.UsuarioControlador;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import servicios.DatabaseStarter;
import servicios.FormularioServicio;
import servicios.FormularioServicio;

import java.sql.SQLException;

public class Main {

    private static String modoConexion = "";
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

        new UsuarioControlador(app).aplicarRutas();
        new FormularioControlador(app).aplicarRuta();
    }
    public static String getConecction(){
        return modoConexion;
    }

}
