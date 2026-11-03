package gt.edu.umg.controller;

import gt.edu.umg.core.entities.Medicamento;
import gt.edu.umg.service.impl.MedicamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/medicamentos")
@Tag(name = "Medicamentos", description = "Endpoints para el inventario de medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    @GetMapping
    @Operation(summary = "Listar todos los medicamentos")
    public List<Medicamento> listarTodos() {
        return medicamentoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener medicamento por ID")
    public ResponseEntity<Medicamento> obtenerPorId(@PathVariable Long id) {
        return medicamentoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo medicamento")
    public ResponseEntity<Medicamento> crear(@RequestBody Medicamento medicamento) {
        if (medicamento.getStockDisponible() == null) {
            medicamento.setStockDisponible(0);
        }
        Medicamento nuevo = medicamentoService.guardar(medicamento);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar medicamento existente")
    public ResponseEntity<Medicamento> actualizar(@PathVariable Long id, @RequestBody Medicamento m) {
        return medicamentoService.obtenerPorId(id).map(existente -> {
            existente.setNombre(m.getNombre());
            existente.setPresentacion(m.getPresentacion());
            existente.setDosis(m.getDosis());
            existente.setStockDisponible(m.getStockDisponible());
            return ResponseEntity.ok(medicamentoService.guardar(existente));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar medicamento por ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        medicamentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
