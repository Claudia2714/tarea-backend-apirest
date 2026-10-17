package gt.edu.umg.dao;

import gt.edu.umg.core.entities.Medico; // Importa tu entidad
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Long> {
    // Spring Data JPA ya incluye métodos como save(), findAll(), findById(), deleteById()
}
