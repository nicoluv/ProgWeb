package modelos;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class VentasProductos {
    private long id;
    private Date fechaCompra;
    private String nombreCliente;
    private ArrayList<Producto> listaProductos;

    public VentasProductos(long id, Date fechaCompra, String nombreCliente, ArrayList<Producto> listaProductos) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.nombreCliente = nombreCliente;
        this.listaProductos = listaProductos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public ArrayList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ArrayList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }

    public String getFechaFormateada(){
        return new SimpleDateFormat("dd/MM/yyyy").format(this.fechaCompra);
    }

    public double getTotal(){
        double total = 0;

        for (Producto producto: listaProductos) {
            total += producto.getPrecio() * producto.getCantidad();
        }

        return total;
    }
}
