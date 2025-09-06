package bo.com.bisa.evaluacion.dto;

import bo.com.bisa.evaluacion.modelo.Periodicidad;
import lombok.Data;

@Data
public class PeticionActualizarBlog {
    private String nombre;
    private String tema;
    private String contenido;
    private Periodicidad periodicidad;
    private Boolean permiteComentarios;
}