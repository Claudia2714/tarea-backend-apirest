package gt.edu.umg.service.impl;

import gt.edu.umg.core.entities.Medico;
import gt.edu.umg.dao.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicoService {

    @Autowired
    private MedicoRepository medicoRepository;

    // Obtener todos
    public List<Medico> obtenerTodos() {
        return medicoRepository.findAll();
    }

    // Guardar o actualizar
    public Medico guardar(Medico medico) {
        return medicoRepository.save(medico);
    }

    // Buscar por ID
    public Optional<Medico> obtenerPorId(Long id) {
        return medicoRepository.findById(id);
    }

    // Eliminar
    public void eliminar(Long id) {
        medicoRepository.deleteById(id);
    }
}
