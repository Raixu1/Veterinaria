package app.repository;

import app.models.OrdenMedica;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenMedicaRepository extends JpaRepository<OrdenMedica, Long> {
    List<OrdenMedica> findByMascota_Id(Long idMascota);
}