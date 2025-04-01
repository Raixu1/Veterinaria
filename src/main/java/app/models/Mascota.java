package app.models;

import jakarta.persistence.*;
import jakarta.persistence.Id;

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private int edad;

    @Column(nullable = false)
    private String especie;

    @Column(nullable = false)
    private String raza;

    @Column(length = 255) 
    private String caracteristicas;  

    @Column(nullable = false)
    private double peso;

    @ManyToOne
    @JoinColumn(name = "dueno_id", nullable = false)
    private Persona dueno; 

 
    public Mascota() {}

    public Mascota(String nombre, Persona dueno, int edad, String especie, String raza, String caracteristicas, double peso) {
        this.nombre = nombre;
        this.dueno = dueno;
        this.edad = edad;
        this.especie = especie;
        this.raza = raza;
        this.caracteristicas = caracteristicas;
        this.peso = peso;
    }

    public Mascota(String nombre, Persona dueno, int edad, String especie, String raza, double peso) {
        this(nombre, dueno, edad, especie, raza, null, peso);
    }

    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }

    public String getEspecie() { return especie; }
    public void setEspecie(String especie) { this.especie = especie; }

    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public String getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(String caracteristicas) { this.caracteristicas = caracteristicas; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public Persona getDueno() { return dueno; }
    public void setDueno(Persona dueno) { this.dueno = dueno; }
}
