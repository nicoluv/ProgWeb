package modelos;

import jakarta.persistence.*;

@Entity
public class Formulario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String nombre;
    @Column
    private String n_grado;
    @Column
    private String sector;
    @OneToOne
    private Ubicacion ubicacion;

    public Formulario() {
    }

    public Formulario(String nombre, String n_grado, String sector, Ubicacion ubicacion) {
        this.nombre = nombre;
        this.n_grado = n_grado;
        this.sector = sector;
        this.ubicacion = ubicacion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getN_grado() {
        return n_grado;
    }

    public void setN_grado(String n_grado) {
        this.n_grado = n_grado;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }
}
