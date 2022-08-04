package servicios;

import modelos.Formulario;
import modelos.Ubicacion;

public class UbicacionServicio extends DBManager<Ubicacion>{
    private static UbicacionServicio instancia;

    public static UbicacionServicio getInstancia(){
        if(instancia == null){
            return new UbicacionServicio();
        }
        return instancia;
    }

    private UbicacionServicio(){
        super(Ubicacion.class);
    }
}
