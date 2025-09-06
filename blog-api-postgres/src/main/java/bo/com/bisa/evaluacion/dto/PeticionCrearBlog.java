package bo.com.bisa.evaluacion.dto;

import bo.com.bisa.evaluacion.modelo.Autor;
import bo.com.bisa.evaluacion.modelo.Periodicidad;
import lombok.Data;

@Data
public class PeticionCrearBlog {
    private Autor autor;
    private String nombre;
    private String tema;
    private String contenido;
    private Periodicidad periodicidad;
    private boolean permiteComentarios;
}
