package modelos;

import java.util.ArrayList;

public class CarroCompra {
    private long id;
    private ArrayList<Producto> listaProductos = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public void setProducto(Producto producto){
        this.listaProductos.add(producto);
    }
}
