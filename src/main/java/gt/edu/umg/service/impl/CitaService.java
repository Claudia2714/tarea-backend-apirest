package gt.edu.umg.service.impl;

import gt.edu.umg.core.entities.Cita;
import gt.edu.umg.dao.CitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CitaService {

    @Autowired
    private CitaRepository citaRepository;

    public List<Cita> obtenerTodas() {
        return citaRepository.findAll();
    }

    public Optional<Cita> obtenerPorId(Long id) {
        return citaRepository.findById(id);
    }

    public Cita guardar(Cita cita) {
        return citaRepository.save(cita);
    }

    public void eliminar(Long id) {
        citaRepository.deleteById(id);
    }

    public List<Cita> buscarPorFecha(LocalDate fecha) {
        return citaRepository.findByFecha(fecha);
    }

    public List<Cita> buscarPorPaciente(Long pacienteId) {
        return citaRepository.findByPacienteId(pacienteId);
    }

    public List<Cita> buscarPorMedico(Long medicoId) {
        return citaRepository.findByMedicoId(medicoId);
    }

    /**
     * Valida si el médico ya tiene una cita activa en la misma fecha y hora.
     */
    public boolean tieneConflictoMedico(Long medicoId, LocalDate fecha, String hora, Long citaIdExcluir) {
        List<Cita> citas = citaRepository.findByMedicoIdAndFechaAndHora(medicoId, fecha, hora);
        return citas.stream()
                .filter(c -> !"CANCELADA".equalsIgnoreCase(c.getEstado()))
                .anyMatch(c -> citaIdExcluir == null || !c.getId().equals(citaIdExcluir));
    }

    /**
     * Valida si el paciente ya tiene una cita activa en la misma fecha y hora.
     */
    public boolean tieneConflictoPaciente(Long pacienteId, LocalDate fecha, String hora, Long citaIdExcluir) {
        List<Cita> citas = citaRepository.findByPacienteIdAndFechaAndHora(pacienteId, fecha, hora);
        return citas.stream()
                .filter(c -> !"CANCELADA".equalsIgnoreCase(c.getEstado()))
                .anyMatch(c -> citaIdExcluir == null || !c.getId().equals(citaIdExcluir));
    }
}
