package app.service;

import app.models.RegistroClinico;
import app.models.Mascota;
import app.repository.MascotaRepository;
import app.repository.RegistroClinicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RegistroClinicoService {

    @Autowired
    private RegistroClinicoRepository registroClinicoRepository;

    @Autowired
    private MascotaRepository mascotaRepository;  

    @Transactional
    public RegistroClinico guardarRegistro(RegistroClinico registro) {
        Mascota mascota = mascotaRepository.findById(registro.getMascota().getId())
                .orElseThrow(() -> new IllegalArgumentException("Mascota no encontrada"));

        registro.setMascota(mascota);

   
        if ("vacunación".equalsIgnoreCase(registro.getProcedimiento())) {
            if (!"perro".equalsIgnoreCase(mascota.getEspecie()) && !"gato".equalsIgnoreCase(mascota.getEspecie())) {
                throw new IllegalArgumentException("El historial de vacunación solo aplica para perros y gatos.");
            }

     
            if (registro.getHistorialVacunacion() == null || registro.getHistorialVacunacion().isEmpty()) {
                throw new IllegalArgumentException("Debe ingresar el nombre de la vacuna aplicada.");
            }
        } else {
            registro.setHistorialVacunacion(null);
        }

        return registroClinicoRepository.save(registro);
    }
    public List<RegistroClinico> obtenerTodosLosRegistros() {
        return registroClinicoRepository.findAll();
    }
    public RegistroClinico obtenerRegistroPorId(Long id) {
        return registroClinicoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Registro clínico no encontrado"));
    }
    public void eliminarRegistro(Long id) {
        if (!registroClinicoRepository.existsById(id)) {
            throw new IllegalArgumentException("Registro clínico no encontrado");
        }
        registroClinicoRepository.deleteById(id);
    }
}
