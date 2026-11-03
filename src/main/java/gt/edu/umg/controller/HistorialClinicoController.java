package gt.edu.umg.controller;

import gt.edu.umg.core.entities.Cita;
import gt.edu.umg.core.entities.Medicamento;
import gt.edu.umg.core.entities.Paciente;
import gt.edu.umg.core.entities.Receta;
import gt.edu.umg.dao.CitaRepository;
import gt.edu.umg.dao.MedicamentoRepository;
import gt.edu.umg.dao.PacienteRepository;
import gt.edu.umg.dao.RecetaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/historial")
@Tag(name = "Historial Clínico", description = "Endpoints para consultar el historial clínico y recetas del paciente")
public class HistorialClinicoController {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    @GetMapping("/paciente/{pacienteId}")
    @Operation(summary = "Obtener el expediente/historial clínico completo del paciente")
    public ResponseEntity<?> obtenerHistorialPaciente(@PathVariable Long pacienteId) {
        Optional<Paciente> pacOpt = pacienteRepository.findById(pacienteId);
        if (pacOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Paciente paciente = pacOpt.get();
        List<Cita> citas = citaRepository.findByPacienteId(pacienteId);
        // Ordenar consultas por fecha descendente (más recientes primero)
        citas.sort((a, b) -> b.getFecha().compareTo(a.getFecha()));

        List<Map<String, Object>> consultasConRecetas = new ArrayList<>();
        for (Cita c : citas) {
            Map<String, Object> item = new HashMap<>();
            item.put("cita", c);
            List<Receta> recetas = recetaRepository.findByCitaId(c.getId());
            item.put("recetas", recetas);
            consultasConRecetas.add(item);
        }

        Map<String, Object> expediente = new HashMap<>();
        expediente.put("paciente", paciente);
        expediente.put("totalConsultas", citas.size());
        expediente.put("consultas", consultasConRecetas);

        return ResponseEntity.ok(expediente);
    }

    @PostMapping("/recetas")
    @Operation(summary = "Prescribir medicamento en una cita médica")
    public ResponseEntity<?> agregarReceta(@RequestBody Map<String, Object> body) {
        try {
            Long citaId = Long.valueOf(body.get("citaId").toString());
            Long medicamentoId = Long.valueOf(body.get("medicamentoId").toString());
            String indicaciones = body.get("indicaciones").toString();
            Integer cantidad = body.get("cantidad") != null ? Integer.valueOf(body.get("cantidad").toString()) : 1;

            Cita cita = citaRepository.findById(citaId)
                    .orElseThrow(() -> new IllegalArgumentException("Cita médica no encontrada con ID: " + citaId));
            Medicamento med = medicamentoRepository.findById(medicamentoId)
                    .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado con ID: " + medicamentoId));

            // Si hay stock disponible, descontar del inventario
            if (med.getStockDisponible() != null && med.getStockDisponible() >= cantidad) {
                med.setStockDisponible(med.getStockDisponible() - cantidad);
                medicamentoRepository.save(med);
            }

            Receta receta = new Receta();
            receta.setCita(cita);
            receta.setMedicamento(med);
            receta.setIndicaciones(indicaciones);
            receta.setCantidad(cantidad);

            Receta guardada = recetaRepository.save(receta);
            return new ResponseEntity<>(guardada, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/recetas/{id}")
    @Operation(summary = "Eliminar receta prescrita")
    public ResponseEntity<Void> eliminarReceta(@PathVariable Long id) {
        recetaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
