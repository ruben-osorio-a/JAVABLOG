package bo.com.bisa.evaluacion.configuracion;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfiguracionSwagger {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Blog API")
                        .version("1.0.0")
                        .description("API REST para la gestión de Blogs con PostgreSQL")
                        .contact(new Contact()
                                .name("BISA")
                                .email("contacto@bisa.com")
                                .url("https://www.bisa.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}
