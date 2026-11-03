package gt.edu.umg.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "recetas")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cita_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Cita cita;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medicamento_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Medicamento medicamento;

    @Column(nullable = false, length = 255)
    private String indicaciones; // Ej: 1 cada 8 horas por 5 días

    @Column(nullable = false)
    private Integer cantidad = 1;
}

