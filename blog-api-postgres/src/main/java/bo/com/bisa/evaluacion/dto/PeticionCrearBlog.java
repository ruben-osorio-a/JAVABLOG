package bo.com.bisa.evaluacion.dto;

import bo.com.bisa.evaluacion.modelo.Autor;
import bo.com.bisa.evaluacion.modelo.Periodicidad;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PeticionCrearBlog {
    
    @NotNull(message = "El autor es obligatorio")
    @Valid
    private Autor autor;
    
    @NotBlank(message = "El nombre del blog es obligatorio")
    @Size(min = 3, max = 255, message = "El nombre del blog debe tener entre 3 y 255 caracteres")
    private String nombre;
    
    @Size(max = 100, message = "El tema no puede exceder 100 caracteres")
    private String tema;
    
    @NotBlank(message = "El contenido del blog es obligatorio")
    @Size(min = 10, max = 5000, message = "El contenido debe tener entre 10 y 5000 caracteres")
    private String contenido;
    
    @NotNull(message = "La periodicidad es obligatoria")
    private Periodicidad periodicidad;
    
    private boolean permiteComentarios;
}
