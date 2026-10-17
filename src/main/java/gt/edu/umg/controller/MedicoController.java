package gt.edu.umg.controller;

import gt.edu.umg.core.entities.Medico;
import gt.edu.umg.service.impl.MedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/medicos")
public class MedicoController {

    @Autowired
    private MedicoService medicoService;

    @GetMapping
    public List<Medico> listarTodos() {
        return medicoService.obtenerTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medico> obtenerPorId(@PathVariable Long id) {
        return medicoService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Medico> crear(@RequestBody Medico medico) {
        Medico nuevoMedico = medicoService.guardar(medico);
        return new ResponseEntity<>(nuevoMedico, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        medicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}