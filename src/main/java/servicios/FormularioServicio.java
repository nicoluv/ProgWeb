package servicios;

import modelos.Formulario;

public class FormularioServicio extends DBManager<Formulario>{
    private static FormularioServicio instancia;

    public static FormularioServicio getInstancia(){
        if(instancia == null){
            return new FormularioServicio();
        }
        return instancia;
    }

    private FormularioServicio(){
        super(Formulario.class);
    }
}
