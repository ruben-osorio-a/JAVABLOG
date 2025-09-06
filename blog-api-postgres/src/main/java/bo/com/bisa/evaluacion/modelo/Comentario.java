package bo.com.bisa.evaluacion.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comentario")
@Data
@NoArgsConstructor
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    @JsonBackReference
    private Blog blog;

    @Column(nullable = false)
    private String nombreComentarista;

    @Column(nullable = false)
    private String correoComentarista;

    private String paisResidenciaComentarista;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String contenido;

    @Column(nullable = false)
    private int puntuacion;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaPublicacion;
}

