# 🐳 Instrucciones de Docker

Este documento contiene instrucciones detalladas para construir y ejecutar la aplicación Blog API usando Docker.

## 📋 Prerrequisitos

- Docker instalado (versión 20.10 o superior)
- Docker Compose instalado (versión 2.0 o superior)
- Maven instalado (para construir el proyecto)

## 🚀 Opciones de Despliegue

### Opción 1: Script Automatizado (Recomendado)

```bash
# Hacer el script ejecutable
chmod +x build-and-run.sh

# Ejecutar el script completo
./build-and-run.sh
```

Este script:
1. Compila el proyecto con Maven
2. Construye la imagen Docker
3. Inicia todos los servicios con Docker Compose
4. Muestra las URLs de acceso

### Opción 2: Comandos Manuales

#### 1. Construir el Proyecto
```bash
mvn clean package -DskipTests
```

#### 2. Construir la Imagen Docker
```bash
docker build -t blog-api:latest .
```

#### 3. Ejecutar con Docker Compose
```bash
# Iniciar todos los servicios
docker-compose up -d

# Ver logs en tiempo real
docker-compose logs -f blog-api

# Detener servicios
docker-compose down
```

### Opción 3: Solo la Aplicación (sin PostgreSQL)

```bash
# Construir la imagen
docker build -t blog-api .

# Ejecutar solo la aplicación (requiere PostgreSQL externo)
docker run -p 8861:8861 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/blogdb \
  -e SPRING_DATASOURCE_USERNAME=postgres \
  -e SPRING_DATASOURCE_PASSWORD=postgres123 \
  blog-api
```

## 🔧 Configuración de Variables de Entorno

### Variables Disponibles

| Variable | Descripción | Valor por Defecto |
|----------|-------------|-------------------|
| `SPRING_DATASOURCE_URL` | URL de conexión a PostgreSQL | `jdbc:postgresql://postgres:5432/blogdb` |
| `SPRING_DATASOURCE_USERNAME` | Usuario de la base de datos | `postgres` |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de la base de datos | `postgres123` |
| `SERVER_PORT` | Puerto del servidor | `8861` |
| `SPRING_PROFILES_ACTIVE` | Perfil activo de Spring | `default` |

### Ejemplo con Variables Personalizadas

```bash
docker run -p 8861:8861 \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://mi-postgres:5432/mi-blogdb \
  -e SPRING_DATASOURCE_USERNAME=mi_usuario \
  -e SPRING_DATASOURCE_PASSWORD=mi_password_seguro \
  -e SERVER_PORT=8080 \
  blog-api
```

## 📊 Monitoreo y Logs

### Ver Logs de la Aplicación
```bash
# Logs en tiempo real
docker-compose logs -f blog-api

# Logs de todos los servicios
docker-compose logs -f

# Últimas 100 líneas de logs
docker-compose logs --tail=100 blog-api
```

### Verificar Estado de los Servicios
```bash
# Estado de todos los servicios
docker-compose ps

# Health check de la aplicación
curl http://localhost:8861/actuator/health

# Health check de PostgreSQL
docker-compose exec postgres pg_isready -U postgres
```

## 🗄️ Gestión de Base de Datos

### Acceder a PostgreSQL
```bash
# Conectar a la base de datos
docker-compose exec postgres psql -U postgres -d blogdb

# Ejecutar consultas SQL
docker-compose exec postgres psql -U postgres -d blogdb -c "SELECT * FROM blog;"
```

### Backup de la Base de Datos
```bash
# Crear backup
docker-compose exec postgres pg_dump -U postgres blogdb > backup.sql

# Restaurar backup
docker-compose exec -T postgres psql -U postgres blogdb < backup.sql
```

## 🔄 Actualización de la Aplicación

### Actualizar Código y Reconstruir
```bash
# 1. Detener servicios
docker-compose down

# 2. Reconstruir la aplicación
mvn clean package -DskipTests

# 3. Reconstruir imagen Docker
docker build -t blog-api:latest .

# 4. Reiniciar servicios
docker-compose up -d
```

### Actualizar Solo la Aplicación (mantener datos)
```bash
# Reconstruir y reiniciar solo la aplicación
docker-compose up -d --build blog-api
```

## Limpieza

### Limpiar Contenedores y Volúmenes
```bash
# Detener y eliminar contenedores
docker-compose down

# Eliminar también volúmenes (¡CUIDADO! Esto elimina los datos)
docker-compose down -v

# Eliminar imágenes no utilizadas
docker image prune -f

# Limpieza completa del sistema Docker
docker system prune -f
```

## 🐛 Solución de Problemas

### Problema: Puerto ya en uso
```bash
# Verificar qué proceso usa el puerto
lsof -i :8861

# Cambiar puerto en docker-compose.yml
# O detener el proceso que usa el puerto
```

### Problema: Error de conexión a base de datos
```bash
# Verificar que PostgreSQL esté ejecutándose
docker-compose ps postgres

# Ver logs de PostgreSQL
docker-compose logs postgres

# Reiniciar PostgreSQL
docker-compose restart postgres
```

### Problema: Aplicación no inicia
```bash
# Ver logs detallados
docker-compose logs blog-api

# Verificar configuración
docker-compose config

# Reconstruir imagen
docker-compose build --no-cache blog-api
```

## Optimizaciones

### Imagen Multi-stage (Opcional)
Para una imagen más pequeña, puedes usar un Dockerfile multi-stage:

```dockerfile
# Stage 1: Build
FROM maven:3.9-openjdk-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/blog-api-postgres-*.jar app.jar
EXPOSE 8861
ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Variables de JVM
```bash
docker run -p 8861:8861 \
  -e JAVA_OPTS="-Xmx512m -Xms256m" \
  blog-api
```

## Seguridad

### Usar Secrets para Contraseñas
```bash
# Crear archivo de secrets
echo "mi_password_seguro" > db_password.txt

# Usar en docker-compose
docker-compose up -d
```

### Ejecutar como Usuario No-Root
El Dockerfile ya incluye un usuario no-root para mayor seguridad.

## Soporte

Si encuentras problemas:
1. Revisa los logs: `docker-compose logs -f`
2. Verifica la configuración: `docker-compose config`
3. Consulta la documentación de Docker
4. Revisa los issues del proyecto
