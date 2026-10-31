package gt.edu.umg.service.impl;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import gt.edu.umg.core.entities.Paciente;
import gt.edu.umg.dao.PacienteRepository;

@Service
public class PacienteServiceImpl implements PacienteService {

	private final PacienteRepository pacienteRepository;

	public PacienteServiceImpl(PacienteRepository pacienteRepository) {
		this.pacienteRepository = pacienteRepository;
	}
	
	@Override
	@Transactional
	public Paciente guardar(Paciente paciente) {
		return pacienteRepository.save(paciente);
	}

	@Override
	@Transactional
	public Paciente crear(Paciente paciente) {
		paciente.setId(null);
		if (paciente.getActivo() == null) {
			paciente.setActivo(true);
		}
		return pacienteRepository.save(paciente);
	}

	@Override
	@Transactional
	public Paciente actualizar(Long id, Paciente datos) {
		Paciente existente = obtenerPorId(id);

		if (datos.getNombre() != null && !datos.getNombre().isBlank()) {
			existente.setNombre(datos.getNombre());
		}
		if (datos.getApellido() != null && !datos.getApellido().isBlank()) {
			existente.setApellido(datos.getApellido());
		}
		if (datos.getDpi() != null && !datos.getDpi().isBlank()) {
			existente.setDpi(datos.getDpi());
		}
		if (datos.getTelefono() != null && !datos.getTelefono().isBlank()) {
			existente.setTelefono(datos.getTelefono());
		}
		if (datos.getCorreo() != null && !datos.getCorreo().isBlank()) {
			existente.setCorreo(datos.getCorreo());
		}
		if (datos.getFechaNacimiento() != null) {
			existente.setFechaNacimiento(datos.getFechaNacimiento());
		}
		if (datos.getActivo() != null) {
			existente.setActivo(datos.getActivo());
		}

		return pacienteRepository.save(existente);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Paciente> obtenerTodos() {
		return pacienteRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Paciente obtenerPorId(Long id) {
		return pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado con el ID: " + id));
	}

	@Override
	@Transactional
	public void eliminar(Long id) {
		if (!pacienteRepository.existsById(id)) {
            throw new RuntimeException("No se puede eliminar. El paciente no existe con ID: " + id);
        }
        pacienteRepository.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Paciente> buscarConFiltro(Specification<Paciente> spec) {
		return pacienteRepository.findAll(spec);
	}

}
