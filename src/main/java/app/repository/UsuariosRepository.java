package app.repository;

import app.models.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuariosRepository extends JpaRepository<Usuarios, String> {
    Optional<Usuarios> findByCedula(String cedula);
    Optional<Usuarios> findByUsername(String username); 
}

