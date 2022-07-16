package servicios;

import modelos.VentasProductos;
import modelos.ProdVentas;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class VentasServicio extends DBManager<VentasProductos> {

    private static VentasServicio instancia;
    private List<VentasProductos> ventas = new ArrayList<>();

    private VentasServicio(){
        super(VentasProductos.class);
    }

    public static VentasServicio getInstancia(){
        if(instancia ==null){
            return new VentasServicio();
        }
        return instancia;
    }

    public List<VentasProductos> getVentas(){
        ventas = findAll();
        return ventas;
    }

    public void setVentas(String nombre, LocalDate fecha, List<ProdVentas> listaProductos){
        crear(new VentasProductos(fecha, nombre, listaProductos));
    }





}
