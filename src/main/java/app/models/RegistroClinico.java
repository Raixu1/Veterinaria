package app.models;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "registros_clinicos")
public class RegistroClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long id;
    
    private LocalDate fecha;
   
    @Version
    private Long version;
    
    @ManyToOne
    @JoinColumn(name = "orden_medica_id")
    private OrdenMedica ordenMedica;

    @ManyToOne
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @ManyToOne
    @JoinColumn(name = "veterinario_id", nullable = false)
    private Usuarios veterinario;

    private String motivoConsulta;
    private String sintomatologia;
    private String diagnostico;
    private String procedimiento; 
    private String medicamento;
    private String dosis;
    
    @Column(unique = true)
    private String idOrden; 

    private String historialVacunacion; 
    private String medicamentosAlergia;
    private String detalleProcedimiento;
    private boolean anulacionOrden;

    public RegistroClinico() {}
    
    public RegistroClinico(LocalDate fecha, Mascota mascota, Usuarios veterinario, String motivoConsulta, 
                           String sintomatologia, String diagnostico, String procedimiento, String medicamento, 
                           String dosis, String idOrden, String historialVacunacion, String medicamentosAlergia, 
                           String detalleProcedimiento, boolean anulacionOrden) {
        this.fecha = fecha;  
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.motivoConsulta = motivoConsulta;
        this.sintomatologia = sintomatologia;
        this.diagnostico = diagnostico;
        this.procedimiento = procedimiento;
        this.medicamento = medicamento;
        this.dosis = dosis;
        this.idOrden = idOrden;
        this.historialVacunacion = historialVacunacion;
        this.medicamentosAlergia = medicamentosAlergia;
        this.detalleProcedimiento = detalleProcedimiento;
        this.anulacionOrden = anulacionOrden;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Usuarios getVeterinario() {
        return veterinario;
    }

    public void setVeterinario(Usuarios veterinario) {
        this.veterinario = veterinario;
    }

    public String getMotivoConsulta() {
        return motivoConsulta;
    }

    public void setMotivoConsulta(String motivoConsulta) {
        this.motivoConsulta = motivoConsulta;
    }

    public String getSintomatologia() {
        return sintomatologia;
    }

    public void setSintomatologia(String sintomatologia) {
        this.sintomatologia = sintomatologia;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getProcedimiento() {
        return procedimiento;
    }

    public void setProcedimiento(String procedimiento) {
        if (procedimiento.equalsIgnoreCase("vacunacion") && !(mascota.getEspecie().equalsIgnoreCase("Perro") || mascota.getEspecie().equalsIgnoreCase("Gato"))) {
            throw new IllegalArgumentException("Solo se puede realizar vacunación en perros y gatos.");
        }
        this.procedimiento = procedimiento;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(String idOrden) {
        this.idOrden = idOrden;
    }

    public String getHistorialVacunacion() {
        return historialVacunacion;
    }

    public void setHistorialVacunacion(String historialVacunacion) {
        this.historialVacunacion = historialVacunacion;
    }

    public String getMedicamentosAlergia() {
        return medicamentosAlergia;
    }

    public void setMedicamentosAlergia(String medicamentosAlergia) {
        this.medicamentosAlergia = medicamentosAlergia;
    }

    public String getDetalleProcedimiento() {
        return detalleProcedimiento;
    }

    public void setDetalleProcedimiento(String detalleProcedimiento) {
        this.detalleProcedimiento = detalleProcedimiento;
    }

    public boolean isAnulacionOrden() {
        return anulacionOrden;
    }

    public void setAnulacionOrden(boolean anulacionOrden) {
        this.anulacionOrden = anulacionOrden;
    }
}
