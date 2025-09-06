package bo.com.bisa.evaluacion.repositorio;

import bo.com.bisa.evaluacion.modelo.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Blog.
 * Proporciona metodos CRUD (Crear, Leer, Actualizar, Borrar) para los blogs
 * gracias a la herencia de JpaRepository.
 */
@Repository
public interface BlogRepositorio extends JpaRepository<Blog, Long> {
}
