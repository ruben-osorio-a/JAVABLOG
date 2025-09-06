package bo.com.bisa.evaluacion.servicio;

import bo.com.bisa.evaluacion.dto.PeticionActualizarBlog;
import bo.com.bisa.evaluacion.dto.PeticionCrearBlog;
import bo.com.bisa.evaluacion.dto.PeticionCrearComentario;
import bo.com.bisa.evaluacion.excepcion.ExcepcionLogicaNegocio;
import bo.com.bisa.evaluacion.excepcion.ExcepcionRecursoNoEncontrado;
import bo.com.bisa.evaluacion.modelo.Blog;
import bo.com.bisa.evaluacion.modelo.Comentario;
import bo.com.bisa.evaluacion.modelo.HistorialBlog;
import bo.com.bisa.evaluacion.repositorio.BlogRepositorio;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BlogServicio {

    private final BlogRepositorio blogRepositorio;

    @Transactional
    public Blog crear(PeticionCrearBlog peticion) {
        Blog nuevoBlog = new Blog();
        nuevoBlog.setAutor(peticion.getAutor());
        nuevoBlog.setNombre(peticion.getNombre());
        nuevoBlog.setTema(peticion.getTema());
        nuevoBlog.setContenido(peticion.getContenido());
        nuevoBlog.setPeriodicidad(peticion.getPeriodicidad());
        nuevoBlog.setPermiteComentarios(peticion.isPermiteComentarios());
        nuevoBlog.setFechaCreacion(LocalDateTime.now());
        return blogRepositorio.save(nuevoBlog);
    }

    @Transactional(readOnly = true)
    public List<Blog> obtenerTodos() {
        return blogRepositorio.findAll();
    }

    @Transactional(readOnly = true)
    public Blog obtenerPorId(Long blogId) {
        return blogRepositorio.findById(blogId)
                .orElseThrow(() -> new ExcepcionRecursoNoEncontrado("Blog no encontrado con id: " + blogId));
    }

    @Transactional
    public Blog actualizar(Long blogId, PeticionActualizarBlog peticion) {
        Blog blogExistente = obtenerPorId(blogId);


        HistorialBlog historial = new HistorialBlog();
        historial.setBlog(blogExistente);
        historial.setNombre(blogExistente.getNombre());
        historial.setTema(blogExistente.getTema());
        historial.setContenido(blogExistente.getContenido());
        historial.setFechaModificacion(LocalDateTime.now());
        blogExistente.getHistorial().add(historial);


        blogExistente.setNombre(peticion.getNombre());
        blogExistente.setTema(peticion.getTema());
        blogExistente.setContenido(peticion.getContenido());
        blogExistente.setPeriodicidad(peticion.getPeriodicidad());
        if (peticion.getPermiteComentarios() != null) {
            blogExistente.setPermiteComentarios(peticion.getPermiteComentarios());
        }

        return blogRepositorio.save(blogExistente);
    }

    @Transactional
    public Blog anadirComentario(Long blogId, PeticionCrearComentario peticion) {
        Blog blog = obtenerPorId(blogId);

        if (!blog.isPermiteComentarios()) {
            throw new ExcepcionLogicaNegocio("Este blog no admite comentarios.");
        }
        if (peticion.getPuntuacion() < 0 || peticion.getPuntuacion() > 10) {
            throw new ExcepcionLogicaNegocio("La puntuación debe estar entre 0 y 10.");
        }

        Comentario nuevoComentario = new Comentario();
        nuevoComentario.setBlog(blog);
        nuevoComentario.setNombreComentarista(peticion.getNombreComentarista());
        nuevoComentario.setCorreoComentarista(peticion.getCorreoComentarista());
        nuevoComentario.setPaisResidenciaComentarista(peticion.getPaisResidenciaComentarista());
        nuevoComentario.setContenido(peticion.getContenido());
        nuevoComentario.setPuntuacion(peticion.getPuntuacion());
        nuevoComentario.setFechaPublicacion(LocalDateTime.now());

        blog.getComentarios().add(nuevoComentario);
        return blogRepositorio.save(blog);
    }
}
