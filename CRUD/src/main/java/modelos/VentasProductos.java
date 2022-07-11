package modelos;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
@Entity
public class VentasProductos implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private LocalDate fechaCompra;
    @Column(nullable = false)
    private String nombreCliente;
    @OneToMany(fetch = FetchType.EAGER)
    private List<ProdVentas> listaProductos;

    public VentasProductos(long id, LocalDate fechaCompra, String nombreCliente) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.nombreCliente = nombreCliente;
    }

    public VentasProductos(LocalDate fechaCompra, String nombreCliente, List<ProdVentas> listaProductos) {
        this.fechaCompra = fechaCompra;
        this.nombreCliente = nombreCliente;
        this.listaProductos = listaProductos;
    }

    public VentasProductos() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDate fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<ProdVentas> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(List<ProdVentas> listaProductos) {
        this.listaProductos = listaProductos;
    }


    public double getTotal() {
        double total = 0;

        for (ProdVentas producto : listaProductos) {
            total += producto.getPrecio() * producto.getCantidad();
        }

        return total;
    }
}