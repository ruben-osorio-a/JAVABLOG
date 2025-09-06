package bo.com.bisa.evaluacion.modelo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "historial_blog")
@Data
@NoArgsConstructor
public class HistorialBlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blog_id", nullable = false)
    @JsonBackReference
    private Blog blog;

    @Column(nullable = false)
    private String nombre;

    private String tema;

    @Lob
    private String contenido;

    @Column(nullable = false)
    private LocalDateTime fechaModificacion;
}