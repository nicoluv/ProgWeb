package servicios;

import modelos.ProdVentas;

public class ProdVentasServicio extends DBManager<ProdVentas>{

    private static ProdVentasServicio instancia;

    private ProdVentasServicio(){
        super(ProdVentas.class);
    }

    public static ProdVentasServicio getInstancia(){
        if(instancia == null){
            return new ProdVentasServicio();
        }
        return instancia;
    }

    public void setProdVentas(ProdVentas prodVentas){
        crear(prodVentas);
    }

}
