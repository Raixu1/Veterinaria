package app.models;

import java.util.List;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class OrdenMedica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOrden; 
    
    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;

    @OneToMany(mappedBy = "ordenMedica", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroClinico> registrosClinicos;

    @ManyToOne
    @JoinColumn(name = "cedula_dueno", nullable = false)
    private Persona dueno;

    @ManyToOne
    @JoinColumn(name = "cedula_veterinario", nullable = false)
    private Persona veterinario;

    private String nombreMedicamento;
    private String dosis;
    private LocalDate fechaGeneracion;

    public OrdenMedica(Mascota mascota, Persona dueno, Persona veterinario, String nombreMedicamento, String dosis) {
        this.mascota = mascota;
        this.dueno = dueno;
        this.veterinario = veterinario;
        this.nombreMedicamento = nombreMedicamento;
        this.dosis = dosis;
        this.fechaGeneracion = LocalDate.now();
    }

    public Long getIdOrden() {
        return idOrden;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Persona getDueno() {
        return dueno;
    }

    public void setDueno(Persona dueno) {
        this.dueno = dueno;
    }

    public Persona getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Persona veterinario) {
        this.veterinario = veterinario;
    }

    public String getNombreMedicamento() {
        return nombreMedicamento;
    }

    public void setNombreMedicamento(String nombreMedicamento) {
        this.nombreMedicamento = nombreMedicamento;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }
}
