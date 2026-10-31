package gt.edu.umg.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import gt.edu.umg.core.entities.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long>, JpaSpecificationExecutor<Paciente> {

}
