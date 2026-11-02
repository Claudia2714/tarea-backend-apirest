package gt.edu.umg.core.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "citas")
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "paciente_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Paciente paciente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Medico medico;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, length = 10)
    private String hora; // Formato HH:mm ej: 09:30

    @Column(nullable = false, length = 255)
    private String motivo;

    @Column(nullable = false, length = 30)
    private String estado = "PROGRAMADA"; // PROGRAMADA, ATENDIDA, CANCELADA

    @PrePersist
    public void prePersist() {
        if (this.estado == null || this.estado.isBlank()) {
            this.estado = "PROGRAMADA";
        }
    }
}
