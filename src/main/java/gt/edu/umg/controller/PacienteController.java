package gt.edu.umg.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import gt.edu.umg.core.entities.Paciente;
import gt.edu.umg.service.impl.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping({"/api/pacientes", "/api/v1/pacientes"})
@Tag(name = "Pacientes", description = "Endpoints para la gestión de pacientes")
public class PacienteController {

	private final PacienteService pacienteService;

	public PacienteController(PacienteService pacienteService) {
		this.pacienteService = pacienteService;
	}

	@PostMapping
	@Operation(summary = "Crear un nuevo paciente")
	public ResponseEntity<Paciente> crear(@RequestBody Paciente paciente) {
		Paciente creado = pacienteService.crear(paciente);
		return new ResponseEntity<>(creado, HttpStatus.CREATED);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Modificar un paciente existente")
	public ResponseEntity<Paciente> actualizar(@PathVariable Long id, @RequestBody Paciente paciente) {
		Paciente actualizado = pacienteService.actualizar(id, paciente);
		return ResponseEntity.ok(actualizado);
	}

	@GetMapping
	@Operation(summary = "Obtener lista de todos los pacientes")
	public ResponseEntity<List<Paciente>> obtenerTodos() {
		return ResponseEntity.ok(pacienteService.obtenerTodos());
	}

	@GetMapping("/{id}")
	@Operation(summary = "Obtener un paciente por su ID")
	public ResponseEntity<Paciente> obtenerPorId(@PathVariable Long id) {
		Paciente paciente = pacienteService.obtenerPorId(id);
		return ResponseEntity.ok(paciente);
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar un paciente por su ID")
	public ResponseEntity<Void> eliminar(@PathVariable Long id) {
		pacienteService.eliminar(id);
		return ResponseEntity.noContent().build();
	}
}
