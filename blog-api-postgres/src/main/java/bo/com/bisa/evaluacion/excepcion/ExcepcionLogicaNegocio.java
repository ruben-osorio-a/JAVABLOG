package bo.com.bisa.evaluacion.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ExcepcionLogicaNegocio extends RuntimeException {
    public ExcepcionLogicaNegocio(String message) {
        super(message);
    }
}
