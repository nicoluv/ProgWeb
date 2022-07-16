package servicios;

import modelos.Foto;

import java.util.Set;

public class FotoServicio extends DBManager<Foto>{
    private static FotoServicio instancia;

    public static FotoServicio getInstancia(){
        if(instancia == null){
            return new FotoServicio();
        }
        return instancia;
    }

    private FotoServicio(){
        super(Foto.class);
    }

    public void setFoto(Foto foto){
        crear(foto);
    }

    public void setListaFotos(Set<Foto> fotos){
        for (Foto foto: fotos) {
            crear(foto);
        }
    }

}
