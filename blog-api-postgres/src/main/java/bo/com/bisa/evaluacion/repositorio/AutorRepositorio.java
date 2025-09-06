package bo.com.bisa.evaluacion.repositorio;

import bo.com.bisa.evaluacion.modelo.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, Long> {
    
    /**
     * Busca un autor por su correo electrónico
     * @param correoElectronico el correo electrónico del autor
     * @return Optional con el autor si existe
     */
    Optional<Autor> findByCorreoElectronico(String correoElectronico);
    
    /**
     * Verifica si existe un autor con el correo electrónico dado
     * @param correoElectronico el correo electrónico a verificar
     * @return true si existe, false en caso contrario
     */
    boolean existsByCorreoElectronico(String correoElectronico);
}
