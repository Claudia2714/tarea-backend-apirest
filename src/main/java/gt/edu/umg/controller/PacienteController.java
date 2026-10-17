package gt.edu.umg.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gt.edu.umg.core.entities.Paciente;
import gt.edu.umg.core.entities.Dtos.Request.PacienteRequestDto;
import gt.edu.umg.core.entities.Dtos.Response.PacienteResponseDto;
import gt.edu.umg.mapper.PacienteMapper;
import gt.edu.umg.service.impl.PacienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/pacientes")
@Tag(name = "Pacientes", description = "Endpoints para la gestión de pacientes")
public class PacienteController {

	private final PacienteService pacienteService;
	private final PacienteMapper pacienteMapper;
	
	
	public PacienteController(PacienteService pacienteService, PacienteMapper pacienteMapper) {
		this.pacienteService = pacienteService;
		this.pacienteMapper = pacienteMapper;
	}
	
	@PostMapping
    public ResponseEntity<PacienteResponseDto> crear(@RequestBody PacienteRequestDto request) {
        // 1. Controller usa el Mapper: RequestDto -> Entity
        Paciente entidad = pacienteMapper.toEntity(request);

        // 2. Controller llama al Service pasando únicamente la Entity
        Paciente guardado = pacienteService.guardar(entidad);

        // 3. Controller usa el Mapper: Entity -> ResponseDto
        return new ResponseEntity<>(pacienteMapper.toDto(guardado), HttpStatus.CREATED);
    }
	
	@GetMapping
    @Operation(summary = "Obtener lista de todos los pacientes")
    public ResponseEntity<List<PacienteResponseDto>> obtenerTodos() {
        List<PacienteResponseDto> respuesta = pacienteService.obtenerTodos()
                .stream()
                .map(pacienteMapper::toDto)
                .toList();

        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un paciente por su ID")
    public ResponseEntity<PacienteResponseDto> obtenerPorId(@PathVariable Long id) {
        Paciente paciente = pacienteService.obtenerPorId(id);
        return ResponseEntity.ok(pacienteMapper.toDto(paciente));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un paciente por su ID")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        pacienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
	
}
