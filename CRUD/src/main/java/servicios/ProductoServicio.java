package servicios;

import modelos.CarroCompra;
import modelos.Producto;
import modelos.VentasProductos;
import java.util.Date;
import java.util.ArrayList;


public class ProductoServicio {
    private static ProductoServicio instancia;
    private final CarroCompra carrito = new CarroCompra();

    private ArrayList<Producto> productos = new ArrayList<>();
    private ArrayList<VentasProductos> ventas = new ArrayList<>();

    public ProductoServicio(){
        productos.add(new Producto( "Kimchi enlatado", 375));
        productos.add(new Producto("Rice paper", 120));
        productos.add(new Producto("Pockys te verde", 210));
        productos.add(new Producto("wonton congelado", 600));
    }

    public static ProductoServicio getInstancia(){
        if(instancia == null)
            instancia = new ProductoServicio();

        return instancia;
    }


    public ArrayList<Producto> getProductos(){
        return this.productos;
    }

    public void agregarProductosCarrito(Producto producto){
        carrito.setProducto(producto);
    }

    public ArrayList<Producto> getProductosCarrito(){
        return carrito.getListaProductos();
    }

    public void setVentas(String nombre, Date fecha, ArrayList<Producto> productos){
        ventas.add(new VentasProductos(ventas.size()+1, fecha, nombre, productos));
    }

    public void validarProducto(String id, int cantidad){

        for (Producto producto: carrito.getListaProductos()) {
            if(producto.getId().equals(id)){
                producto.setCantidad(producto.getCantidad()+cantidad);
                return;
            }
        }

        for (Producto producto: productos) {
            if(producto.getId().equals(id)){
                producto.setCantidad(cantidad);
                agregarProductosCarrito(producto);
                return;
            }
        }

    }

    public double getTotal(){
        double total = 0;

        for (Producto producto: carrito.getListaProductos()) {
            total += producto.getPrecio() * producto.getCantidad();
        }

        return total;
    }

    public ArrayList<VentasProductos> getVentas(){
        return ventas;
    }

}
