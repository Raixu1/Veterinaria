package app.repository;

import app.models.Mascota;
import app.models.RegistroClinico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RegistroClinicoRepository extends JpaRepository<RegistroClinico, Long> {
    List<RegistroClinico> findByMascota(Mascota mascota);
}
