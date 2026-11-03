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

import java.time.LocalDate;
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
    @Operation(summary = "Crear nueva cita médica con validaciones de negocio")
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body) {
        try {
            if (body.get("pacienteId") == null || body.get("medicoId") == null ||
                body.get("fecha") == null || body.get("hora") == null || body.get("motivo") == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "Todos los campos (paciente, médico, fecha, hora, motivo) son requeridos."));
            }

            Long pacienteId = Long.valueOf(body.get("pacienteId").toString());
            Long medicoId = Long.valueOf(body.get("medicoId").toString());
            String fechaStr = body.get("fecha").toString();
            String hora = body.get("hora").toString().trim();
            String motivo = body.get("motivo").toString().trim();
            String estado = body.get("estado") != null ? body.get("estado").toString() : "PROGRAMADA";

            // 1. Regla: Paciente debe existir y estar activo en la base de datos
            Paciente paciente = pacienteRepository.findById(pacienteId)
                    .orElseThrow(() -> new IllegalArgumentException("El paciente seleccionado no se encuentra registrado en el sistema."));
            if (paciente.getActivo() != null && !paciente.getActivo()) {
                return ResponseEntity.badRequest().body(Map.of("error", "No se puede agendar cita: El paciente se encuentra inactivo."));
            }

            // 2. Regla: Médico debe existir en la base de datos
            Medico medico = medicoRepository.findById(medicoId)
                    .orElseThrow(() -> new IllegalArgumentException("El médico seleccionado no existe o no se encuentra disponible."));

            LocalDate fecha = LocalDate.parse(fechaStr);

            // 3. Regla: Disponibilidad de horario del médico (evitar solapamiento)
            if (citaService.tieneConflictoMedico(medicoId, fecha, hora, null)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                        "error", String.format("El médico %s no tiene disponibilidad el día %s a las %s horas (ya cuenta con una cita programada).",
                                medico.getNombre(), fecha, hora)
                ));
            }

            // 4. Regla: Disponibilidad del paciente en esa misma fecha y hora
            if (citaService.tieneConflictoPaciente(pacienteId, fecha, hora, null)) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of(
                        "error", String.format("El paciente %s %s ya tiene otra cita agendada para el día %s a las %s.",
                                paciente.getNombre(), paciente.getApellido(), fecha, hora)
                ));
            }

            Cita cita = new Cita();
            cita.setPaciente(paciente);
            cita.setMedico(medico);
            cita.setFecha(fecha);
            cita.setHora(hora);
            cita.setMotivo(motivo);
            cita.setEstado(estado);

            Cita guardada = citaService.guardar(cita);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Error al procesar la cita: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cita médica existente")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        try {
            return citaService.obtenerPorId(id).map(citaExistente -> {
                Long medicoId = citaExistente.getMedico().getId();
                Long pacienteId = citaExistente.getPaciente().getId();
                LocalDate fecha = citaExistente.getFecha();
                String hora = citaExistente.getHora();

                if (body.get("pacienteId") != null) {
                    pacienteId = Long.valueOf(body.get("pacienteId").toString());
                    Paciente p = pacienteRepository.findById(pacienteId)
                            .orElseThrow(() -> new IllegalArgumentException("Paciente no encontrado."));
                    citaExistente.setPaciente(p);
                }
                if (body.get("medicoId") != null) {
                    medicoId = Long.valueOf(body.get("medicoId").toString());
                    Medico m = medicoRepository.findById(medicoId)
                            .orElseThrow(() -> new IllegalArgumentException("Médico no encontrado."));
                    citaExistente.setMedico(m);
                }
                if (body.get("fecha") != null) {
                    fecha = LocalDate.parse(body.get("fecha").toString());
                    citaExistente.setFecha(fecha);
                }
                if (body.get("hora") != null) {
                    hora = body.get("hora").toString().trim();
                    citaExistente.setHora(hora);
                }
                if (body.get("motivo") != null) {
                    citaExistente.setMotivo(body.get("motivo").toString());
                }
                if (body.get("estado") != null) {
                    citaExistente.setEstado(body.get("estado").toString());
                }

                // Si no se cancela, validar que no choque horario
                if (!"CANCELADA".equalsIgnoreCase(citaExistente.getEstado())) {
                    if (citaService.tieneConflictoMedico(medicoId, fecha, hora, id)) {
                        throw new IllegalStateException("Conflicto de horario: El médico ya tiene otra cita en esa fecha y hora.");
                    }
                }

                Cita actualizada = citaService.guardar(citaExistente);
                return ResponseEntity.ok(actualizada);
            }).orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", e.getMessage()));
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
