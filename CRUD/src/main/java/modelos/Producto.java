package modelos;

import java.util.UUID;

public class Producto {
    private String id;
    private String nombre;
    private double precio;
    private int cantidad;

    public Producto(String nombre, double precio) {
        this.id = UUID.randomUUID().toString();;
        this.nombre = nombre;
        this.precio = precio;
        cantidad = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotal(){
        return cantidad * precio;
    }
}
