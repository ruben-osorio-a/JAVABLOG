package bo.com.bisa.evaluacion.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "comentario")
@Data
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    @JsonIgnore // Evita la serializacion recursiva
    private Blog blog;

    @Column(name = "nombre_comentarista", nullable = false)
    private String nombreComentarista;

    @Column(name = "correo_comentarista", nullable = false)
    private String correoComentarista;

    @Column(name = "pais_residencia_comentarista", nullable = false)
    private String paisResidenciaComentarista;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(nullable = false)
    private int puntuacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}
