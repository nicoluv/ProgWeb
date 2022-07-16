package modelos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import java.time.LocalDate;


@Entity
public class Comentario {
    @Id
    @GeneratedValue
    private Long id;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String mensaje;
    @Column(nullable = false)
    private LocalDate fecha;


    public Comentario() {

    }


    public Comentario(String nombre, String mensaje, LocalDate fecha) {
        this.nombre = nombre;
        this.mensaje = mensaje;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
