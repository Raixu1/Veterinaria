package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import app.models.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {}