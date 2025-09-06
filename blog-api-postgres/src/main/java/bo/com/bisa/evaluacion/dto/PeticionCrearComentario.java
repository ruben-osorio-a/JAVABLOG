package bo.com.bisa.evaluacion.dto;

import lombok.Data;

@Data
public class PeticionCrearComentario {
    private String nombreComentarista;
    private String correoComentarista;
    private String paisResidenciaComentarista;
    private String contenido;
    private int puntuacion;
}
