package gt.edu.umg.dao;

import gt.edu.umg.core.entities.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    List<Receta> findByCitaId(Long citaId);
    List<Receta> findByCitaPacienteId(Long pacienteId);
}
