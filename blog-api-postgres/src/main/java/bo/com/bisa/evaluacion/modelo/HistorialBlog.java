package bo.com.bisa.evaluacion.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_blog")
@Data
public class HistorialBlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    @JsonIgnore
    private Blog blog;

    @Column(name = "nombre_anterior")
    private String nombreAnterior;

    @Column(name = "tema_anterior")
    private String temaAnterior;

    @Column(name = "contenido_anterior", columnDefinition = "TEXT")
    private String contenidoAnterior;

    @Column(name = "periodicidad_anterior")
    @Enumerated(EnumType.STRING)
    private Periodicidad periodicidadAnterior;

    @Column(name = "permite_comentarios_anterior")
    private Boolean permiteComentariosAnterior;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
}
