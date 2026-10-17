package gt.edu.umg.core.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data // Lombok genera los getters, setters y constructores automáticamente
@Entity
@Table(name = "medicos")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String especialidad;

    @Column(nullable = false, unique = true, length = 20)
    private String numeroColegiado;
    
    @Column(length = 15)
    private String telefono;
}
