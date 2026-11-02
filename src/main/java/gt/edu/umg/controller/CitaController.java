package gt.edu.umg.controller;

import gt.edu.umg.core.entities.Cita;
import gt.edu.umg.core.entities.Medico;
import gt.edu.umg.core.entities.Paciente;
import gt.edu.umg.dao.MedicoRepository;
import gt.edu.umg.dao.PacienteRepository;
import gt.edu.umg.service.impl.CitaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/citas")
@Tag(name = "Citas", description = "Endpoints para la gestión de citas médicas")
public class CitaController {

    @Autowired
    private CitaService citaService;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @GetMapping
    @Operation(summary = "Listar todas las citas")
    public List<Cita> listarTodas() {
        return citaService.obtenerTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cita por ID")
    public ResponseEntity<Cita> obtenerPorId(@PathVariable Long id) {
        return citaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nueva cita médica")
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        try {
            Long pacienteId = Long.valueOf(body.get("pacienteId").toString());
            Long medicoId = Long.valueOf(body.get("medicoId").toString());
            String fechaStr = body.get("fecha").toString();
            String hora = body.get("hora").toString();
            String motivo = body.get("motivo").toString();
            String estado = body.get("estado") != null ? body.get("estado").toString() : "PROGRAMADA";

            Paciente paciente = pacienteRepository.findById(pacienteId)
                    .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado con ID: " + pacienteId));
            Medico medico = medicoRepository.findById(medicoId)
                    .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado con ID: " + medicoId));

            Cita cita = new Cita();
            cita.setPaciente(paciente);
            cita.setMedico(medico);
            cita.setFecha(java.time.LocalDate.parse(fechaStr));
            cita.setHora(hora);
            cita.setMotivo(motivo);
            cita.setEstado(estado);

            Cita guardada = citaService.guardar(cita);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cita médica existente")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            return citaService.obtenerPorId(id).map(citaExistente -> {
                if (body.get("pacienteId") != null) {
                    Long pacienteId = Long.valueOf(body.get("pacienteId").toString());
                    pacienteRepository.findById(pacienteId).ifPresent(citaExistente::setPaciente);
                }
                if (body.get("medicoId") != null) {
                    Long medicoId = Long.valueOf(body.get("medicoId").toString());
                    medicoRepository.findById(medicoId).ifPresent(citaExistente::setMedico);
                }
                if (body.get("fecha") != null) {
                    citaExistente.setFecha(java.time.LocalDate.parse(body.get("fecha").toString()));
                }
                if (body.get("hora") != null) {
                    citaExistente.setHora(body.get("hora").toString());
                }
                if (body.get("motivo") != null) {
                    citaExistente.setMotivo(body.get("motivo").toString());
                }
                if (body.get("estado") != null) {
                    citaExistente.setEstado(body.get("estado").toString());
                }
                Cita actualizada = citaService.guardar(citaExistente);
                return ResponseEntity.ok(actualizada);
            }).orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cita por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        citaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
