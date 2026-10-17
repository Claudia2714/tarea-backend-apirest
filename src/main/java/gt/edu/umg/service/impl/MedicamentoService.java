package gt.edu.umg.service.impl;

import gt.edu.umg.core.entities.Medicamento;
import gt.edu.umg.dao.MedicamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicamentoService {

    @Autowired
    private MedicamentoRepository medicamentoRepository;

    // Obtener todos los medicamentos
    public List<Medicamento> obtenerTodos() {
        return medicamentoRepository.findAll();
    }

    // Guardar o actualizar un medicamento
    public Medicamento guardar(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    // Buscar un medicamento por su ID
    public Optional<Medicamento> obtenerPorId(Long id) {
        return medicamentoRepository.findById(id);
    }

    // Eliminar un medicamento
    public void eliminar(Long id) {
        medicamentoRepository.deleteById(id);
    }
}