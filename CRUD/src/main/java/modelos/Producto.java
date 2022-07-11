package modelos;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.*;

@Entity
public class Producto implements Serializable {
    @Id
    private String id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private double precio;
    @Column(nullable = false)
    private int cantidad;
    @Column
    private String descripcion;
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Foto> fotos = new HashSet<>();
    @OneToMany(fetch = FetchType.EAGER)
    private List<Comentario> comentarios = new ArrayList<>();

    public Producto() {
    }

    public Producto(String id, String nombre, double precio, int cantidad, String descripcion, Set<Foto> fotos, List<Comentario> comentarios) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
        this.fotos = fotos;
        this.comentarios = comentarios;
    }


    public Producto(String nombre, double precio) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.precio = precio;
        cantidad = 0;
    }

    public Producto(String nombre, double precio, Set<Foto> fotos, String descripcion) {
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.precio = precio;
        this.fotos = fotos;
        this.descripcion = descripcion;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Foto> getFotos() {
        return fotos;
    }

    public void setFotos(Set<Foto> fotos) {
        this.fotos = fotos;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }
}
