package gt.edu.umg.controller;

import gt.edu.umg.core.entities.Medicamento;
import gt.edu.umg.service.impl.MedicamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;

    @GetMapping
    public List<Medicamento> listarTodos() {
        return medicamentoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicamento> obtenerPorId(@PathVariable Long id) {
        return medicamentoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Medicamento> crear(@RequestBody Medicamento medicamento) {
        Medicamento nuevoMedicamento = medicamentoService.guardar(medicamento);
        return new ResponseEntity<>(nuevoMedicamento, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        medicamentoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
