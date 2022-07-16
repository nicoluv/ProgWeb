package servicios;

import modelos.Comentario;

public class ComentarioServicio extends DBManager<Comentario>{
    private static ComentarioServicio instancia;

    public static ComentarioServicio getInstancia(){
        if (instancia==null){
            return new ComentarioServicio();
        }
        return instancia;
    }

    private ComentarioServicio(){

        super(Comentario.class);

    }






}
