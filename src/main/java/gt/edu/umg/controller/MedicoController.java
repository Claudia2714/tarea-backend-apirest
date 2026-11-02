package gt.edu.umg.controller;

import gt.edu.umg.core.entities.Medico;
import gt.edu.umg.service.impl.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/medicos")
@Tag(name = "Medicos", description = "Endpoints para la gestión de médicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    @Operation(summary = "Listar todos los médicos")
    public List<Medico> listarTodos() {
        return medicoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener médico por ID")
    public ResponseEntity<Medico> obtenerPorId(@PathVariable Long id) {
        return medicoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo médico")
    public ResponseEntity<Medico> crear(@RequestBody Medico medico) {
        Medico nuevoMedico = medicoService.guardar(medico);
        return new ResponseEntity<>(nuevoMedico, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar médico existente")
    public ResponseEntity<Medico> actualizar(@PathVariable Long id, @RequestBody Medico medico) {
        return medicoService.obtenerPorId(id).map(m -> {
            m.setNombre(medico.getNombre());
            m.setEspecialidad(medico.getEspecialidad());
            m.setNumeroColegiado(medico.getNumeroColegiado());
            m.setTelefono(medico.getTelefono());
            Medico actualizado = medicoService.guardar(m);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar médico por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        medicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
