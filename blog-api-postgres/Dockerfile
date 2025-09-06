# Usar imagen base de OpenJDK 21
FROM openjdk:21-jdk-slim

# Metadatos de la imagen
LABEL maintainer="ruben.osorio.it@outlook.com"
LABEL description="Blog API - Sistema de gestión de blogs modo Evaluación BISA"
LABEL version="1.0.0"

# Crear directorio de trabajo
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

# Variables de entorno por defecto
ENV SPRING_PROFILES_ACTIVE=default
ENV SERVER_PORT=8861

# Health check para verificar que la aplicación esté funcionando
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8861/actuator/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", \
  "-jar", \
  "app.jar"]
