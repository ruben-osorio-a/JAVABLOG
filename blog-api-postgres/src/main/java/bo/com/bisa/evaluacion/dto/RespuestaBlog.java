package bo.com.bisa.evaluacion.dto;

import bo.com.bisa.evaluacion.modelo.Blog;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.IntSummaryStatistics;

@Data
@NoArgsConstructor
public class RespuestaBlog {

    private Blog blog;
    private ResumenPuntuaciones resumenPuntuaciones;

    public RespuestaBlog(Blog blog) {
        this.blog = blog;
        if (blog.getComentarios() != null && !blog.getComentarios().isEmpty()) {
            this.resumenPuntuaciones = new ResumenPuntuaciones(blog.getComentarios().stream()
                    .mapToInt(c -> c.getPuntuacion())
                    .summaryStatistics());
        }
    }

    @Data
    @NoArgsConstructor
    private static class ResumenPuntuaciones {
        private int minimo;
        private int maximo;
        private double promedio;

        public ResumenPuntuaciones(IntSummaryStatistics stats) {
            this.minimo = stats.getMin();
            this.maximo = stats.getMax();
            this.promedio = stats.getAverage();
        }
    }
}