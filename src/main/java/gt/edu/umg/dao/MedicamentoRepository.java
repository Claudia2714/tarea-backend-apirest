package gt.edu.umg.dao;

import gt.edu.umg.core.entities.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    // Con esto ya tenemos acceso a la base de datos sin escribir SQL
}