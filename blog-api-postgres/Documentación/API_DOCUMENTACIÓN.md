# 📚 Documentación de la API - Blog para Evaluación BISA

Esta documentación complementa la interfaz Swagger UI proporcionando ejemplos prácticos, casos de uso y guías de integración.

## Visión General

La API de Blog para Evaluación BISA permite gestionar blogs, autores y comentarios de manera completa. Está diseñada para ser RESTful, segura y fácil de integrar.

### Características Principales
- **Gestión de Blogs**: CRUD completo con validaciones robustas
- **Sistema de Comentarios**: Comentarios con puntuación y validaciones
- **Gestión de Autores**: Reutilización automática de autores existentes
- **Historial de Cambios**: Seguimiento automático de modificaciones
- **Validaciones Multi-capa**: Bean Validation + reglas de negocio

## 🚀 Inicio Rápido

### Base URL
```
http://localhost:8861/api/v1
```

### Autenticación
Actualmente la API no requiere autenticación, pero está preparada para implementarla.

### Headers Requeridos
```http
Content-Type: application/json
Accept: application/json
```

## 📋 Endpoints Detallados

### 1. Gestión de Blogs

#### Crear un Nuevo Blog
```http
POST /api/v1/blogs
```

**Descripción**: Crea un nuevo blog con autor. Si el autor ya existe (por correo), se reutiliza automáticamente.

**Ejemplo de Request**:
```json
{
  "autor": {
    "nombres": "María Elena",
    "apellidoPaterno": "Rodríguez",
    "apellidoMaterno": "González",
    "fechaNacimiento": "1985-03-15",
    "paisResidencia": "Bolivia",
    "correoElectronico": "maria.rodriguez@email.com"
  },
  "nombre": "Tecnología y Desarrollo Web",
  "tema": "Programación",
  "contenido": "En este blog compartiré mis experiencias como desarrolladora de software, tutoriales sobre tecnologías modernas como Spring Boot, React, Node.js y mejores prácticas en el desarrollo web.",
  "periodicidad": "SEMANAL",
  "permiteComentarios": true
}
```

**Ejemplo de Response (201 Created)**:
```json
{
  "id": 1,
  "autor": {
    "id": 1,
    "nombres": "María Elena",
    "apellidoPaterno": "Rodríguez",
    "apellidoMaterno": "González",
    "fechaNacimiento": "1985-03-15",
    "paisResidencia": "Bolivia",
    "correoElectronico": "maria.rodriguez@email.com"
  },
  "nombre": "Tecnología y Desarrollo Web",
  "tema": "Programación",
  "contenido": "En este blog compartiré mis experiencias...",
  "periodicidad": "SEMANAL",
  "permiteComentarios": true,
  "comentarios": [],
  "historial": [],
  "fechaCreacion": "2025-09-06T18:11:25.813253"
}
```

**Validaciones**:
- ✅ Nombre del blog único
- ✅ Autor con datos válidos
- ✅ Contenido entre 10-5000 caracteres
- ✅ Periodicidad válida (DIARIA, SEMANAL, MENSUAL)

#### Obtener Todos los Blogs
```http
GET /api/v1/blogs
```

**Descripción**: Retorna una lista paginada de todos los blogs con resumen de puntuaciones.

**Ejemplo de Response (200 OK)**:
```json
[
  {
    "blog": {
      "id": 1,
      "autor": { /* datos del autor */ },
      "nombre": "Tecnología y Desarrollo Web",
      "tema": "Programación",
      "contenido": "En este blog compartiré...",
      "periodicidad": "SEMANAL",
      "permiteComentarios": true,
      "comentarios": [
        {
          "id": 1,
          "nombreComentarista": "Pedro Ramírez",
          "correoComentarista": "pedro@email.com",
          "paisResidenciaComentarista": "Bolivia",
          "contenido": "Excelente blog!",
          "puntuacion": 9,
          "fechaPublicacion": "2025-09-06T19:00:00"
        }
      ],
      "historial": [],
      "fechaCreacion": "2025-09-06T18:11:25.813253"
    },
    "resumenPuntuaciones": {
      "promedio": 8.5,
      "totalComentarios": 3,
      "distribucion": {
        "5": 0, "6": 0, "7": 1, "8": 1, "9": 1, "10": 0
      }
    }
  }
]
```

#### Obtener Blog por ID
```http
GET /api/v1/blogs/{id}
```

**Parámetros**:
- `id` (path, required): ID del blog

**Ejemplo de Response (200 OK)**:
```json
{
  "blog": { /* datos completos del blog */ },
  "resumenPuntuaciones": { /* resumen de puntuaciones */ }
}
```

**Errores**:
- `404 Not Found`: Blog no encontrado

#### Actualizar Blog
```http
PUT /api/v1/blogs/{id}
```

**Descripción**: Actualiza un blog existente y crea automáticamente un registro en el historial.

**Ejemplo de Request**:
```json
{
  "nombre": "Tecnología y Desarrollo Web - Actualizado",
  "tema": "Desarrollo de Software",
  "contenido": "Contenido actualizado con más información...",
  "periodicidad": "DIARIA",
  "permiteComentarios": true
}
```

**Validaciones**:
- ✅ Al menos un campo debe ser proporcionado
- ✅ Validaciones de tamaño para campos proporcionados
- ✅ Historial automático de cambios

### 2. Gestión de Comentarios

#### Agregar Comentario a Blog
```http
POST /api/v1/blogs/{id}/comentarios
```

**Descripción**: Agrega un comentario con puntuación a un blog específico.

**Ejemplo de Request**:
```json
{
  "nombreComentarista": "Pedro Ramírez",
  "correoComentarista": "pedro.ramirez@email.com",
  "paisResidenciaComentarista": "Bolivia",
  "contenido": "Excelente blog! Me encanta cómo explicas los conceptos de Spring Boot de manera clara y sencilla. ¿Podrías hacer un tutorial sobre Spring Security?",
  "puntuacion": 9
}
```

**Validaciones**:
- ✅ Blog debe permitir comentarios
- ✅ Puntuación entre 0-10
- ✅ Máximo 100 comentarios por blog
- ✅ Contenido entre 5-1000 caracteres

**Ejemplo de Response (200 OK)**:
```json
{
  "id": 1,
  "autor": { /* datos del autor */ },
  "nombre": "Tecnología y Desarrollo Web",
  "tema": "Programación",
  "contenido": "En este blog compartiré...",
  "periodicidad": "SEMANAL",
  "permiteComentarios": true,
  "comentarios": [
    {
      "id": 1,
      "nombreComentarista": "Pedro Ramírez",
      "correoComentarista": "pedro.ramirez@email.com",
      "paisResidenciaComentarista": "Bolivia",
      "contenido": "Excelente blog! Me encanta...",
      "puntuacion": 9,
      "fechaPublicacion": "2025-09-06T19:00:00"
    }
  ],
  "historial": [],
  "fechaCreacion": "2025-09-06T18:11:25.813253"
}
```

## 🔄 Casos de Uso Comunes

### Caso 1: Crear un Blog con Autor Nuevo

```bash
# 1. Crear blog con autor nuevo
curl -X POST http://localhost:8861/api/v1/blogs \
  -H "Content-Type: application/json" \
  -d '{
    "autor": {
      "nombres": "Carlos Alberto",
      "apellidoPaterno": "Mendoza",
      "apellidoMaterno": "Vargas",
      "fechaNacimiento": "1990-07-22",
      "paisResidencia": "Bolivia",
      "correoElectronico": "carlos.mendoza@email.com"
    },
    "nombre": "Sabores Bolivianos",
    "tema": "Gastronomía",
    "contenido": "Descubre la rica tradición culinaria de Bolivia...",
    "periodicidad": "DIARIA",
    "permiteComentarios": true
  }'
```

### Caso 2: Reutilizar Autor Existente

```bash
# 2. Crear otro blog con el mismo autor
curl -X POST http://localhost:8861/api/v1/blogs \
  -H "Content-Type: application/json" \
  -d '{
    "autor": {
      "nombres": "Carlos Alberto",
      "apellidoPaterno": "Mendoza",
      "apellidoMaterno": "Vargas",
      "fechaNacimiento": "1990-07-22",
      "paisResidencia": "Bolivia",
      "correoElectronico": "carlos.mendoza@email.com"
    },
    "nombre": "Recetas Tradicionales",
    "tema": "Cocina",
    "contenido": "Más recetas tradicionales bolivianas...",
    "periodicidad": "SEMANAL",
    "permiteComentarios": true
  }'
```

### Caso 3: Agregar Comentarios a un Blog

```bash
# 3. Agregar comentario al primer blog
curl -X POST http://localhost:8861/api/v1/blogs/1/comentarios \
  -H "Content-Type: application/json" \
  -d '{
    "nombreComentarista": "Isabel Morales",
    "correoComentarista": "isabel.morales@email.com",
    "paisResidenciaComentarista": "Bolivia",
    "contenido": "¡Qué delicia de blog! Probé tu receta de salteñas y quedaron espectaculares.",
    "puntuacion": 10
  }'
```

### Caso 4: Actualizar Blog y Ver Historial

```bash
# 4. Actualizar blog
curl -X PUT http://localhost:8861/api/v1/blogs/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Sabores Bolivianos - Actualizado",
    "contenido": "Contenido actualizado con más recetas..."
  }'

# 5. Ver el blog actualizado con historial
curl -X GET http://localhost:8861/api/v1/blogs/1
```

## ⚠️ Manejo de Errores

### Errores de Validación (400 Bad Request)

```json
{
  "timestamp": "2025-09-06T22:30:00.000",
  "status": 400,
  "error": "Validation Failed",
  "message": "Los datos de entrada no son válidos",
  "validationErrors": {
    "nombre": "El nombre del blog es obligatorio",
    "contenido": "El contenido debe tener entre 10 y 5000 caracteres",
    "autor.correoElectronico": "El formato del correo electrónico no es válido"
  }
}
```

### Errores de Lógica de Negocio (400 Bad Request)

```json
{
  "timestamp": "2025-09-06T22:30:00.000",
  "status": 400,
  "error": "Bad Request",
  "message": "Ya existe un blog con el nombre: Mi Blog de Tecnología"
}
```

### Recurso No Encontrado (404 Not Found)

```json
{
  "timestamp": "2025-09-06T22:30:00.000",
  "status": 404,
  "error": "Not Found",
  "message": "Blog no encontrado con id: 999"
}
```

### Error del Servidor (500 Internal Server Error)

```json
{
  "timestamp": "2025-09-06T22:30:00.000",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Ha ocurrido un error interno del servidor"
}
```

## Mejores Prácticas

### Para Desarrolladores que Consumen la API

1. **Manejo de Errores**:
   ```javascript
   try {
     const response = await fetch('/api/v1/blogs', {
       method: 'POST',
       headers: { 'Content-Type': 'application/json' },
       body: JSON.stringify(blogData)
     });
     
     if (!response.ok) {
       const error = await response.json();
       console.error('Error:', error.message);
       if (error.validationErrors) {
         console.error('Validation errors:', error.validationErrors);
       }
     }
   } catch (error) {
     console.error('Network error:', error);
   }
   ```

2. **Validación de Datos**:
   - Siempre validar datos antes de enviar
   - Usar los mensajes de error para guiar al usuario
   - Implementar validación en tiempo real

3. **Manejo de Estados**:
   - Mostrar estados de carga durante las operaciones
   - Implementar retry para errores de red
   - Cachear datos cuando sea apropiado

### Para Integración con Frontend

```javascript
// Ejemplo de servicio para React/Vue/Angular
class BlogService {
  constructor(baseURL = 'http://localhost:8861/api/v1') {
    this.baseURL = baseURL;
  }

  async createBlog(blogData) {
    const response = await fetch(`${this.baseURL}/blogs`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(blogData)
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    return await response.json();
  }

  async getBlogs() {
    const response = await fetch(`${this.baseURL}/blogs`);
    return await response.json();
  }

  async addComment(blogId, commentData) {
    const response = await fetch(`${this.baseURL}/blogs/${blogId}/comentarios`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(commentData)
    });
    
    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }
    
    return await response.json();
  }
}
```

## 📊 Límites y Restricciones

### Límites de la API
- **Comentarios por blog**: Máximo 100
- **Longitud de contenido**: 10-5000 caracteres
- **Longitud de comentarios**: 5-1000 caracteres
- **Puntuación**: 0-10 (enteros)

### Validaciones de Unicidad
- **Nombres de blog**: Únicos en todo el sistema
- **Correos de autor**: Únicos en todo el sistema

### Reglas de Negocio
- **Reutilización de autores**: Automática por correo electrónico
- **Historial de cambios**: Automático en actualizaciones
- **Comentarios**: Solo en blogs que los permiten

## 🔧 Configuración y Personalización

### Variables de Entorno
```bash
# Base de datos
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/blogdb
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=password

# Servidor
SERVER_PORT=8861

# Validaciones
BLOG_MAX_COMMENTS=100
BLOG_CONTENT_MIN_LENGTH=10
BLOG_CONTENT_MAX_LENGTH=5000
```

### Personalización de Validaciones
Las validaciones pueden ser personalizadas modificando:
- Anotaciones en los DTOs
- Reglas de negocio en los servicios
- Configuración de la base de datos




## 📞 Soporte y Contacto

### Recursos Adicionales
- **Swagger UI**: `http://localhost:8861/swagger-ui.html`
- **Documentación OpenAPI**: `http://localhost:8861/api-docs`
- **Health Check**: `http://localhost:8861/actuator/health`

### Para Reportar Problemas
1. Verificar que la aplicación esté ejecutándose
2. Revisar los logs de la aplicación
3. Consultar esta documentación
4. Crear un issue en el repositorio

### Para Solicitar Funcionalidades
- Crear un issue con la etiqueta "enhancement"
- Describir el caso de uso
- Proporcionar ejemplos si es posible

---

**Última actualización**: 2025-09-06  
**Versión de la API**: 1.0.0  
**Mantenido por**: Ruben Osorio - ruben.osorio.it@outlook.com
