package bo.com.bisa.evaluacion.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "autor")
@Data
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(min = 2, max = 100, message = "Los nombres deben tener entre 2 y 100 caracteres")
    @Column(nullable = false)
    private String nombres;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 2, max = 50, message = "El apellido paterno debe tener entre 2 y 50 caracteres")
    @Column(name = "apellido_paterno", nullable = false)
    private String apellidoPaterno;

    @Size(max = 50, message = "El apellido materno no puede exceder 50 caracteres")
    @Column(name = "apellido_materno")
    private String apellidoMaterno;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser anterior a la fecha actual")
    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El país de residencia es obligatorio")
    @Size(min = 2, max = 50, message = "El país de residencia debe tener entre 2 y 50 caracteres")
    @Column(name = "pais_residencia", nullable = false)
    private String paisResidencia;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 100, message = "El correo electrónico no puede exceder 100 caracteres")
    @Column(name = "correo_electronico", nullable = false, unique = true)
    private String correoElectronico;
}
