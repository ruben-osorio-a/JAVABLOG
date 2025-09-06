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
    
    /**
     * Verifica si existe un blog con el nombre dado
     * @param nombre el nombre del blog a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByNombre(String nombre);
}
