-- V1__Crear_Tablas_Iniciales.sql

-- Tabla para almacenar a los autores
CREATE TABLE autor (
                       id BIGSERIAL PRIMARY KEY,
                       nombres VARCHAR(100) NOT NULL,
                       apellido_paterno VARCHAR(50) NOT NULL,
                       apellido_materno VARCHAR(50),
                       fecha_nacimiento DATE NOT NULL,
                       pais_residencia VARCHAR(50) NOT NULL,
                       correo_electronico VARCHAR(100) UNIQUE NOT NULL
);

-- Tabla para almacenar los blogs
CREATE TABLE blog (
                      id BIGSERIAL PRIMARY KEY,
                      autor_id BIGINT NOT NULL,
                      nombre VARCHAR(255) NOT NULL,
                      tema VARCHAR(100) NOT NULL,
                      contenido TEXT NOT NULL,
                      periodicidad VARCHAR(20) NOT NULL,
                      permite_comentarios BOOLEAN NOT NULL,
                      fecha_creacion TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                      CONSTRAINT fk_autor FOREIGN KEY (autor_id) REFERENCES autor (id)
);

-- Tabla de los historicos
CREATE TABLE historial_blog (
                                id BIGSERIAL PRIMARY KEY,
                                blog_id BIGINT NOT NULL,
                                nombre_anterior VARCHAR(255),
                                tema_anterior VARCHAR(100),
                                contenido_anterior TEXT,
                                periodicidad_anterior VARCHAR(20),
                                permite_comentarios_anterior BOOLEAN,
                                fecha_actualizacion TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                                CONSTRAINT fk_blog_historial FOREIGN KEY (blog_id) REFERENCES blog (id)
);

-- Tabla para almacenar los comentarios de los blogs
CREATE TABLE comentario (
                            id BIGSERIAL PRIMARY KEY,
                            blog_id BIGINT NOT NULL,
                            nombre_comentarista VARCHAR(100) NOT NULL,
                            correo_comentarista VARCHAR(100) NOT NULL,
                            pais_residencia_comentarista VARCHAR(50) NOT NULL,
                            contenido TEXT NOT NULL,
                            puntuacion INT NOT NULL CHECK (puntuacion >= 0 AND puntuacion <= 10),
                            fecha_creacion TIMESTAMP WITHOUT TIME ZONE NOT NULL,
                            CONSTRAINT fk_blog_comentario FOREIGN KEY (blog_id) REFERENCES blog (id)
);

