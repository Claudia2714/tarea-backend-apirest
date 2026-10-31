package gt.edu.umg.core.entities;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "pacientes")
public class Paciente {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50)
	private String nombre;

	@Column(nullable = false, length = 50)
	private String apellido;

	@Column(nullable = false, unique = true, length = 13)
	private String dpi;

	@Column(nullable = false, length = 15)
	private String telefono;

	@Column(nullable = true)
	private String correo;

	@Column(nullable = true)
	private LocalDate fechaNacimiento;

	@Column(nullable = false)
	private Boolean activo = true;

	public Paciente() {}

	@PrePersist
	public void prePersist() {
		if (this.activo == null) {
			this.activo = true;
		}
		if (this.correo == null || this.correo.isBlank()) {
			this.correo = "paciente@clinica.com";
		}
		if (this.fechaNacimiento == null) {
			this.fechaNacimiento = LocalDate.of(2000, 1, 1);
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	// Compatibilidad con frontend (idPaciente)
	@JsonProperty("idPaciente")
	public Long getIdPaciente() {
		return id;
	}

	@JsonProperty("idPaciente")
	public void setIdPaciente(Long idPaciente) {
		if (idPaciente != null) {
			this.id = idPaciente;
		}
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	// Compatibilidad con frontend (nombres)
	@JsonProperty("nombres")
	public String getNombres() {
		return nombre;
	}

	@JsonProperty("nombres")
	public void setNombres(String nombres) {
		if (nombres != null) {
			this.nombre = nombres;
		}
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	// Compatibilidad con frontend (apellidos)
	@JsonProperty("apellidos")
	public String getApellidos() {
		return apellido;
	}

	@JsonProperty("apellidos")
	public void setApellidos(String apellidos) {
		if (apellidos != null) {
			this.apellido = apellidos;
		}
	}

	public String getDpi() {
		return dpi;
	}

	public void setDpi(String dpi) {
		this.dpi = dpi;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public Boolean getActivo() {
		return activo;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
}
