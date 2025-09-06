package bo.com.bisa.evaluacion.controlador;

import bo.com.bisa.evaluacion.dto.*;
import bo.com.bisa.evaluacion.modelo.Blog;
import bo.com.bisa.evaluacion.servicio.BlogServicio;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/blogs")
@RequiredArgsConstructor
@Tag(name = "Blogs", description = "API para la gestión de blogs")
public class BlogControlador {

    private final BlogServicio blogServicio;

    @Operation(summary = "Crear un nuevo blog", description = "Crea un nuevo blog con la información proporcionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Blog creado exitosamente",
                    content = @Content(schema = @Schema(implementation = Blog.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Blog> crearBlog(@Valid @RequestBody PeticionCrearBlog peticion) {
        Blog nuevoBlog = blogServicio.crear(peticion);
        return new ResponseEntity<>(nuevoBlog, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtener todos los blogs", description = "Retorna una lista de todos los blogs disponibles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de blogs obtenida exitosamente",
                    content = @Content(schema = @Schema(implementation = RespuestaBlog.class))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<List<RespuestaBlog>> obtenerTodosLosBlogs() {
        List<Blog> blogs = blogServicio.obtenerTodos();
        List<RespuestaBlog> respuestas = blogs.stream()
                .map(RespuestaBlog::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(respuestas);
    }

    @Operation(summary = "Obtener blog por ID", description = "Retorna un blog específico basado en su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blog encontrado exitosamente",
                    content = @Content(schema = @Schema(implementation = RespuestaBlog.class))),
            @ApiResponse(responseCode = "404", description = "Blog no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RespuestaBlog> obtenerBlogPorId(
            @Parameter(description = "ID del blog a obtener") @PathVariable Long id) {
        Blog blog = blogServicio.obtenerPorId(id);
        return ResponseEntity.ok(new RespuestaBlog(blog));
    }

    @Operation(summary = "Actualizar blog", description = "Actualiza un blog existente con la información proporcionada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Blog actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = Blog.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Blog no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Blog> actualizarBlog(
            @Parameter(description = "ID del blog a actualizar") @PathVariable Long id, 
            @Valid @RequestBody PeticionActualizarBlog peticion) {
        Blog blogActualizado = blogServicio.actualizar(id, peticion);
        return ResponseEntity.ok(blogActualizado);
    }

    @Operation(summary = "Agregar comentario a blog", description = "Agrega un nuevo comentario a un blog específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comentario agregado exitosamente",
                    content = @Content(schema = @Schema(implementation = Blog.class))),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos"),
            @ApiResponse(responseCode = "404", description = "Blog no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping("/{id}/comentarios")
    public ResponseEntity<Blog> anadirComentario(
            @Parameter(description = "ID del blog al que se agregará el comentario") @PathVariable Long id, 
            @Valid @RequestBody PeticionCrearComentario peticion) {
        Blog blogConNuevoComentario = blogServicio.anadirComentario(id, peticion);
        return ResponseEntity.ok(blogConNuevoComentario);
    }
}
