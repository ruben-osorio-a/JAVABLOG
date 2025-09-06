package bo.com.bisa.evaluacion.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class PeticionCrearComentario {
    
    @NotBlank(message = "El nombre del comentarista es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre del comentarista debe tener entre 2 y 100 caracteres")
    private String nombreComentarista;
    
    @NotBlank(message = "El correo del comentarista es obligatorio")
    @Email(message = "El formato del correo electrónico no es válido")
    @Size(max = 100, message = "El correo electrónico no puede exceder 100 caracteres")
    private String correoComentarista;
    
    @Size(max = 50, message = "El país de residencia no puede exceder 50 caracteres")
    private String paisResidenciaComentarista;
    
    @NotBlank(message = "El contenido del comentario es obligatorio")
    @Size(min = 5, max = 1000, message = "El contenido del comentario debe tener entre 5 y 1000 caracteres")
    private String contenido;
    
    @NotNull(message = "La puntuación es obligatoria")
    @Min(value = 0, message = "La puntuación mínima es 0")
    @Max(value = 10, message = "La puntuación máxima es 10")
    private Integer puntuacion;
}
