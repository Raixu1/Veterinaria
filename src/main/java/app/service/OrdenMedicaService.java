package app.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

import app.models.Usuarios;
import app.models.OrdenMedica;
import app.models.Mascota;
import app.models.RegistroClinico;
import app.repository.OrdenMedicaRepository;
import app.repository.MascotaRepository;
import app.repository.UsuariosRepository;
import app.repository.RegistroClinicoRepository;

@Service
public class OrdenMedicaService {

    private final OrdenMedicaRepository ordenMedicaRepository;
    private final MascotaRepository mascotaRepository;
    private final UsuariosRepository usuariosRepository;
    private final RegistroClinicoRepository registroClinicoRepository;

    @Autowired
    public OrdenMedicaService(OrdenMedicaRepository ordenMedicaRepository, 
                              MascotaRepository mascotaRepository,
                              UsuariosRepository usuariosRepository,
                              RegistroClinicoRepository registroClinicoRepository) {
        this.ordenMedicaRepository = ordenMedicaRepository;
        this.mascotaRepository = mascotaRepository;
        this.usuariosRepository = usuariosRepository;
        this.registroClinicoRepository = registroClinicoRepository;
    }

    public OrdenMedica generarOrdenMedica(Long mascotaId, String veterinarioCedula, String nombreMedicamento, String dosis) {
        Mascota mascota = mascotaRepository.findById(mascotaId)
                .orElseThrow(() -> new IllegalArgumentException("Mascota no encontrada."));

        Usuarios veterinario = usuariosRepository.findByCedula(veterinarioCedula)
                .filter(user -> "VETERINARIO".equals(user.getRol()))
                .orElseThrow(() -> new IllegalArgumentException("Veterinario no encontrado o sin permisos."));

        Usuarios dueno = (Usuarios) mascota.getDueno();
        if (dueno == null) {
            throw new IllegalArgumentException("No se encontró el dueño de la mascota.");
        }

        OrdenMedica ordenMedica = new OrdenMedica(mascota, dueno, veterinario, nombreMedicamento, dosis);
        ordenMedica = ordenMedicaRepository.save(ordenMedica);

        RegistroClinico registroClinico = new RegistroClinico();
    registroClinico.setIdOrden(String.valueOf(ordenMedica.getIdOrden()));
    registroClinico.setFecha(LocalDate.now());
    registroClinico.setMascota(mascota);
    registroClinico.setVeterinario(veterinario);
    registroClinico.setMotivoConsulta("Consulta inicial");
    registroClinico.setSintomatologia("Sintomatología de prueba");
    registroClinico.setDiagnostico("Diagnóstico preliminar");
    registroClinico.setProcedimiento("Consulta general");
    registroClinico.setMedicamento(nombreMedicamento);
    registroClinico.setDosis(dosis);


registroClinicoRepository.save(registroClinico);

        return ordenMedica;
    }

    public List<OrdenMedica> obtenerOrdenesPorMascota(Long idMascota) {
        if (!mascotaRepository.existsById(idMascota)) {
            throw new IllegalArgumentException("Mascota no encontrada.");
        }
        return ordenMedicaRepository.findByMascota_Id(idMascota);
    }
}
