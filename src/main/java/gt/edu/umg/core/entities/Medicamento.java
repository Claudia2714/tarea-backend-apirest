package gt.edu.umg.core.entities;

// Estas son las dos líneas que te faltaban:
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "medicamentos")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private String presentacion; // ej. Pastilla, Jarabe
    private String dosis;
    private Integer stockDisponible;
}