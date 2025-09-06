package bo.com.bisa.evaluacion.controlador;

import bo.com.bisa.evaluacion.dto.*;
import bo.com.bisa.evaluacion.modelo.Blog;
import bo.com.bisa.evaluacion.servicio.BlogServicio;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
public class BlogControlador {

    private final BlogServicio blogServicio;

    @PostMapping
    public ResponseEntity<Blog> crearBlog(@RequestBody PeticionCrearBlog peticion) {
        Blog nuevoBlog = blogServicio.crear(peticion);
        return new ResponseEntity<>(nuevoBlog, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<RespuestaBlog>> obtenerTodosLosBlogs() {
        List<Blog> blogs = blogServicio.obtenerTodos();
        List<RespuestaBlog> respuestas = blogs.stream()
                .map(RespuestaBlog::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuestas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RespuestaBlog> obtenerBlogPorId(@PathVariable Long id) {
        Blog blog = blogServicio.obtenerPorId(id);
        return ResponseEntity.ok(new RespuestaBlog(blog));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Blog> actualizarBlog(@PathVariable Long id, @RequestBody PeticionActualizarBlog peticion) {
        Blog blogActualizado = blogServicio.actualizar(id, peticion);
        return ResponseEntity.ok(blogActualizado);
    }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<Blog> anadirComentario(@PathVariable Long id, @RequestBody PeticionCrearComentario peticion) {
        Blog blogConNuevoComentario = blogServicio.anadirComentario(id, peticion);
        return ResponseEntity.ok(blogConNuevoComentario);
    }
}
