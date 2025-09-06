# Blog API - Sistema de Gestión de Blogs modo Evaluación BISA

Es una API REST para la gestión de blogs con funcionalidades de comentarios, desarrollada con Spring Boot y PostgreSQL.

##  Características Principales

- **Gestión de Blogs**: Crear, leer, actualizar blogs con información
- **Sistema de Comentarios**: Agregar comentarios con puntuación a los blogs
- **Gestión de Autores**: Manejo inteligente de autores con reutilización
- **Historial de Cambios**: Seguimiento de modificaciones en blogs
- **Validaciones Robustas**: Sistema completo de validaciones en múltiples capas, esto para mejorar la calidad de información
- **Documentación Automática**: API documentada con Swagger/OpenAPI
- **Base de Datos**: PostgreSQL con migraciones automáticas con Flyway

##  Tecnologías y Frameworks Utilizados

### Backend Framework
- **Spring Boot 3.2.5**: Framework principal para el desarrollo de la aplicación
  - Spring Web: Para la creación de APIs REST
  - Spring Data JPA: Para el acceso a datos y mapeo objeto-relacional
  - Spring Validation: Para validaciones de entrada de datos
  - Spring Boot DevTools: Para desarrollo con recarga automática

### Base de Datos
- **PostgreSQL 17.6**: Base de datos relacional principal (Alojado en Railway, se exponde datos de conexión para la vlaidación de creación de tablas)
- **Flyway 9.22.3**: Herramienta de migración de base de datos
  - Migraciones versionadas y controladas
  - Scripts SQL organizados en `src/main/resources/db/migration/`

### Mapeo Objeto-Relacional (ORM)
- **Hibernate 6.4.4**: ORM para mapeo de entidades Java a tablas de base de datos
- **JPA (Jakarta Persistence)**: API estándar para persistencia de datos
- **HikariCP**: Pool de conexiones de alto rendimiento

### Validación y Manejo de Datos
- **Bean Validation (Jakarta Validation)**: Validaciones declarativas con anotaciones
- **Hibernate Validator**: Implementación de Bean Validation
- **Validaciones personalizadas**: Reglas de negocio implementadas en servicios

### Documentación de API
- **SpringDoc OpenAPI 2.2.0**: Para la generación automática de documentación
- **Swagger UI**: Interfaz web interactiva para probar la API
- **Anotaciones OpenAPI**: Documentación detallada de endpoints

### Herramientas de Desarrollo
- **Lombok**: Reducción de código boilerplate
- **Maven**: Gestión de dependencias y construcción del proyecto
- **Java 21**: Lenguaje de programación con características modernas

### Contenedorización
- **Docker**: Contenedorización de la aplicación
- **Docker Compose**: Orquestación de servicios (aplicación + PostgreSQL)

## 📋 Requisitos del Sistema

- Java 21 o superior
- Maven 3.6 o superior
- PostgreSQL 15 o superior
- Docker (opcional, para contenedorización)

## 🚀 Instalación y Configuración

### Opción 1: Ejecución Local

1. **Clonar el repositorio**
   ```bash
   git clone <url-del-repositorio>
   cd blog-api-postgres
   ```

2. **Configurar la base de datos**
   - Crear una base de datos PostgreSQL
   - Actualizar las credenciales en : `src/main/resources/application.properties`

3. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

4. **Acceder a la aplicación**
   - API: `http://localhost:8861`
   - Swagger UI: `http://localhost:8861/swagger-ui.html`

### Opción 2: Ejecución con Docker (Recomendado)

#### Método Rápido - Script Automatizado
```bash
# Hacer ejecutable y ejecutar
chmod +x build-and-run.sh
./build-and-run.sh
```

#### Método Manual - Docker Compose
```bash
# 1. Compilar el proyecto
mvn clean package -DskipTests

# 2. Construir imagen Docker
docker build -t blog-api .

# 3. Ejecutar con Docker Compose (incluye PostgreSQL)
docker-compose up -d

# 4. Ver logs
docker-compose logs -f blog-api
```

#### Método Manual - Solo Aplicación
```bash
# Construir y ejecutar solo la aplicación
docker build -t blog-api .
docker run -p 8861:8861 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/blogdb \
  -e SPRING_DATASOURCE_USERNAME=tu_usuario \
  -e SPRING_DATASOURCE_PASSWORD=tu_password \
  blog-api
```

**Acceder a la aplicación:**
- API: `http://localhost:8861`
- Swagger UI: `http://localhost:8861/swagger-ui.html`
- PostgreSQL: `localhost:5432`

> 📖 **Para instrucciones detalladas de Docker**, consulta [DOCKER_INSTRUCIONES.md](Documentación/DOCKER_INSTRUCIONES.md)

## 🐳 Dockerfile

El proyecto incluye un `Dockerfile` optimizado para producción con características de seguridad:

```dockerfile
FROM openjdk:21-jdk-slim

# Metadatos de la imagen
LABEL maintainer="tu-email@ejemplo.com"
LABEL description="Blog API - Sistema de gestión de blogs con Spring Boot"

WORKDIR /app

# Crear usuario no-root para seguridad
RUN groupadd -r appuser && useradd -r -g appuser appuser

# Copiar el archivo JAR construido
COPY target/blog-api-postgres-*.jar app.jar

# Cambiar propietario del archivo JAR
RUN chown appuser:appuser app.jar

# Cambiar al usuario no-root
USER appuser

# Exponer el puerto de la aplicación
EXPOSE 8861

# Health check para verificar que la aplicación esté funcionando
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8861/actuator/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", \
  "-jar", \
  "app.jar"]
```

### Características del Dockerfile

- ✅ **Imagen base optimizada**: OpenJDK 21 slim
- ✅ **Seguridad**: Usuario no-root
- ✅ **Health checks**: Verificación automática de salud
- ✅ **Variables de entorno**: Configuración flexible
- ✅ **Metadatos**: Información del proyecto

## 📚 Estructura del Proyecto

```
src/
├── main/
│   ├── java/bo/com/bisa/evaluacion/
│   │   ├── BlogApiApplication.java          # Clase principal de Spring Boot
│   │   ├── controlador/
│   │   │   └── BlogControlador.java        # Controladores REST
│   │   ├── dto/                            # Objetos de transferencia de datos
│   │   │   ├── PeticionCrearBlog.java
│   │   │   ├── PeticionActualizarBlog.java
│   │   │   ├── PeticionCrearComentario.java
│   │   │   └── RespuestaBlog.java
│   │   ├── excepcion/                      # Manejo de excepciones
│   │   │   ├── ExcepcionLogicaNegocio.java
│   │   │   ├── ExcepcionRecursoNoEncontrado.java
│   │   │   └── ManejadorExcepcionesGlobal.java
│   │   ├── modelo/                         # Entidades JPA
│   │   │   ├── Autor.java
│   │   │   ├── Blog.java
│   │   │   ├── Comentario.java
│   │   │   ├── HistorialBlog.java
│   │   │   └── Periodicidad.java
│   │   ├── repositorio/                    # Repositorios JPA
│   │   │   ├── AutorRepositorio.java
│   │   │   └── BlogRepositorio.java
│   │   ├── servicio/                       # Lógica de negocio
│   │   │   └── BlogServicio.java
│   │   └── configuracion/                  # Configuraciones
│   │       └── ConfiguracionSwagger.java
│   └── resources/
│       ├── application.properties          # Configuración de la aplicación
│       └── db/migration/                   # Scripts de migración Flyway
│           └── V1__Crear_Tablas_Iniciales.sql
└── test/                                   # Pruebas unitarias
```

## 🔧 Configuración

### Variables de Entorno

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `SPRING_DATASOURCE_URL` | URL de conexión a PostgreSQL | `jdbc:postgresql://localhost:5432/blogdb` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de datos | - |
| `SERVER_PORT` | Puerto del servidor | `8861` |

### Configuración de Base de Datos

La aplicación utiliza Flyway para gestionar las migraciones de base de datos. Las migraciones se ejecutan automáticamente al iniciar la aplicación.

## 📖 API Endpoints

### Blogs
- `GET /api/v1/blogs` - Obtener todos los blogs
- `GET /api/v1/blogs/{id}` - Obtener blog por ID
- `POST /api/v1/blogs` - Crear nuevo blog
- `PUT /api/v1/blogs/{id}` - Actualizar blog

### Comentarios
- `POST /api/v1/blogs/{id}/comentarios` - Agregar comentario a blog

### Documentación
- `GET /swagger-ui.html` - Interfaz Swagger UI
- `GET /api-docs` - Documentación JSON de la API

## 📚 Documentación Adicional

### Documentación Completa de la API
- **[API_DOCUMENTACIÓN.md](Documentación/API_DOCUMENTACIÓN.md)** - Documentación detallada con ejemplos, casos de uso y mejores prácticas
- **[GUÍA DE INTEGRACIÓN.md](Documentación/GUÍA DE INTEGRACIÓN.md)** - Guía de integración para desarrolladores con ejemplos en múltiples lenguajes

### Características de la Documentación
- ✅ **Ejemplos prácticos** en JavaScript, Python, Java y PHP
- ✅ **Casos de uso reales** y flujos de trabajo
- ✅ **Guías de integración** paso a paso
- ✅ **Mejores prácticas** de desarrollo
- ✅ **Manejo de errores** robusto
- ✅ **Testing** y monitoreo
- ✅ **Configuración** para diferentes entornos

## 🔍 Validaciones Implementadas

### Validaciones de Entrada
- **Bean Validation**: Validaciones declarativas con anotaciones
- **Validaciones de Negocio**: Reglas específicas del dominio
- **Validaciones de Unicidad**: Prevención de duplicados

### Ejemplos de Validaciones
- Nombres de blog únicos
- Correos electrónicos válidos y únicos
- Puntuaciones entre 0 y 10
- Contenido con longitud mínima y máxima
- Fechas de nacimiento válidas

## 🚨 Manejo de Errores

La aplicación implementa un sistema robusto de manejo de errores:

- **Errores de Validación**: Respuestas detalladas con errores por campo
- **Errores de Negocio**: Mensajes claros sobre reglas violadas
- **Errores de Recurso**: Respuestas 404 para recursos no encontrados
- **Errores del Servidor**: Manejo seguro de errores internos

## 🧪 Pruebas

```bash
# Ejecutar todas las pruebas
mvn test

# Ejecutar pruebas con cobertura
mvn test jacoco:report
```

## Monitoreo y Logs

- **Logs estructurados**: Configuración de logging con Logback
- **Métricas de aplicación**: Endpoints de salud y métricas
- **Trazabilidad**: Logs de transacciones y operaciones

## Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## Autor

- **JUAN RUBEN OSORIO ALCON** - *Desarrollo inicial* - [TuGitHub](https://github.com/ruben-osorio-a)
