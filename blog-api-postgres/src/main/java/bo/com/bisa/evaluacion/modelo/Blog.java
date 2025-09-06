package bo.com.bisa.evaluacion.modelo;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog")
@Data
@NoArgsConstructor
public class Blog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "autor_id", referencedColumnName = "id", nullable = false)
    private Autor autor;

    @Column(nullable = false)
    private String nombre;

    private String tema;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String contenido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Periodicidad periodicidad;

    @Column(nullable = false)
    private boolean permiteComentarios;

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<Comentario> comentarios = new ArrayList<>();

    @OneToMany(mappedBy = "blog", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<HistorialBlog> historial = new ArrayList<>();

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;
}

