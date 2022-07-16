package servicios;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseServicio {
    private static DatabaseServicio instancia;
    private String URL = "jdbc:h2:tcp://localhost/~/tiendaWeb";

    private DatabaseServicio() {
        registrarDriver();
    }

    public static DatabaseServicio getInstancia(){
        if(instancia == null){
            return new DatabaseServicio();
        }
        return instancia;
    }

    private void registrarDriver() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ex) {
            System.out.println("ERROR -No se pudo registrar el driver: "+ex);
        }
    }

    public Connection getConexion(){
        Connection con = null;
        try {
            con = DriverManager.getConnection(URL, "sa", "");
        } catch (SQLException ex) {
            System.out.println("ERROR -No se pudo acceder a la base de datos: "+ex);
        }
        return con;
    }
}
