package gt.edu.umg.dao;

import gt.edu.umg.core.entities.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    List<Cita> findByPacienteId(Long pacienteId);
    List<Cita> findByMedicoId(Long medicoId);
    List<Cita> findByFecha(LocalDate fecha);
    List<Cita> findByMedicoIdAndFechaAndHora(Long medicoId, LocalDate fecha, String hora);
    List<Cita> findByPacienteIdAndFechaAndHora(Long pacienteId, LocalDate fecha, String hora);
}
