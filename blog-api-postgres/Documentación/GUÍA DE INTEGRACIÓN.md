# 🔌 Guía de Integración - Blog API

Esta guía está diseñada para desarrolladores que necesitan integrar la Blog API en sus aplicaciones.

## 🎯 Casos de Uso de Integración

### 1. Aplicación Web Frontend
- **React/Vue/Angular**: Consumir API para mostrar blogs y comentarios
- **Next.js/Nuxt**: Server-side rendering con datos de la API
- **WordPress/Drupal**: Plugin para mostrar blogs externos

### 2. Aplicación Móvil
- **React Native**: App móvil para leer y comentar blogs
- **Flutter**: App multiplataforma
- **iOS/Android Nativo**: Integración directa con HTTP clients

### 3. Microservicios
- **Backend Services**: Integración entre servicios
- **Event-Driven**: Consumir eventos de blogs
- **Data Pipeline**: Sincronización de datos

## 🚀 Ejemplos de Integración por Tecnología

### JavaScript/TypeScript (Frontend)

#### Configuración Base
```javascript
// config/api.js
const API_CONFIG = {
  baseURL: 'http://localhost:8861/api/v1',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
};

// utils/apiClient.js
class ApiClient {
  constructor(config = API_CONFIG) {
    this.baseURL = config.baseURL;
    this.timeout = config.timeout;
    this.headers = config.headers;
  }

  async request(endpoint, options = {}) {
    const url = `${this.baseURL}${endpoint}`;
    const config = {
      ...options,
      headers: { ...this.headers, ...options.headers },
      timeout: this.timeout
    };

    try {
      const response = await fetch(url, config);
      
      if (!response.ok) {
        const error = await response.json();
        throw new ApiError(error.message, response.status, error);
      }
      
      return await response.json();
    } catch (error) {
      if (error instanceof ApiError) throw error;
      throw new ApiError('Network error', 0, error);
    }
  }

  // Métodos HTTP
  get(endpoint, options = {}) {
    return this.request(endpoint, { ...options, method: 'GET' });
  }

  post(endpoint, data, options = {}) {
    return this.request(endpoint, {
      ...options,
      method: 'POST',
      body: JSON.stringify(data)
    });
  }

  put(endpoint, data, options = {}) {
    return this.request(endpoint, {
      ...options,
      method: 'PUT',
      body: JSON.stringify(data)
    });
  }
}

class ApiError extends Error {
  constructor(message, status, details) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
    this.details = details;
  }
}
```

#### Servicio de Blogs
```javascript
// services/blogService.js
import { ApiClient } from '../utils/apiClient';

class BlogService extends ApiClient {
  // Obtener todos los blogs
  async getBlogs() {
    return this.get('/blogs');
  }

  // Obtener blog por ID
  async getBlog(id) {
    return this.get(`/blogs/${id}`);
  }

  // Crear nuevo blog
  async createBlog(blogData) {
    return this.post('/blogs', blogData);
  }

  // Actualizar blog
  async updateBlog(id, blogData) {
    return this.put(`/blogs/${id}`, blogData);
  }

  // Agregar comentario
  async addComment(blogId, commentData) {
    return this.post(`/blogs/${blogId}/comentarios`, commentData);
  }
}

export const blogService = new BlogService();
```

#### Hook de React
```javascript
// hooks/useBlogs.js
import { useState, useEffect } from 'react';
import { blogService } from '../services/blogService';

export const useBlogs = () => {
  const [blogs, setBlogs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchBlogs = async () => {
      try {
        setLoading(true);
        const data = await blogService.getBlogs();
        setBlogs(data);
        setError(null);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchBlogs();
  }, []);

  const createBlog = async (blogData) => {
    try {
      const newBlog = await blogService.createBlog(blogData);
      setBlogs(prev => [...prev, newBlog]);
      return newBlog;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  const addComment = async (blogId, commentData) => {
    try {
      const updatedBlog = await blogService.addComment(blogId, commentData);
      setBlogs(prev => prev.map(blog => 
        blog.id === blogId ? updatedBlog : blog
      ));
      return updatedBlog;
    } catch (err) {
      setError(err.message);
      throw err;
    }
  };

  return { blogs, loading, error, createBlog, addComment };
};
```

#### Componente de React
```jsx
// components/BlogList.jsx
import React from 'react';
import { useBlogs } from '../hooks/useBlogs';

const BlogList = () => {
  const { blogs, loading, error } = useBlogs();

  if (loading) return <div>Cargando blogs...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div className="blog-list">
      {blogs.map(({ blog, resumenPuntuaciones }) => (
        <div key={blog.id} className="blog-card">
          <h2>{blog.nombre}</h2>
          <p><strong>Tema:</strong> {blog.tema}</p>
          <p><strong>Autor:</strong> {blog.autor.nombres} {blog.autor.apellidoPaterno}</p>
          <p><strong>Contenido:</strong> {blog.contenido}</p>
          <p><strong>Puntuación promedio:</strong> {resumenPuntuaciones?.promedio || 'N/A'}</p>
          <p><strong>Comentarios:</strong> {blog.comentarios.length}</p>
        </div>
      ))}
    </div>
  );
};

export default BlogList;
```

### Python (Backend/Microservicios)

#### Cliente HTTP con requests
```python
# blog_client.py
import requests
import json
from typing import Dict, List, Optional
from dataclasses import dataclass

@dataclass
class BlogClient:
    base_url: str = "http://localhost:8861/api/v1"
    timeout: int = 10
    
    def _make_request(self, method: str, endpoint: str, data: Optional[Dict] = None) -> Dict:
        url = f"{self.base_url}{endpoint}"
        headers = {"Content-Type": "application/json"}
        
        try:
            response = requests.request(
                method=method,
                url=url,
                json=data,
                headers=headers,
                timeout=self.timeout
            )
            response.raise_for_status()
            return response.json()
        except requests.exceptions.RequestException as e:
            raise Exception(f"API request failed: {e}")

    def get_blogs(self) -> List[Dict]:
        """Obtener todos los blogs"""
        return self._make_request("GET", "/blogs")

    def get_blog(self, blog_id: int) -> Dict:
        """Obtener blog por ID"""
        return self._make_request("GET", f"/blogs/{blog_id}")

    def create_blog(self, blog_data: Dict) -> Dict:
        """Crear nuevo blog"""
        return self._make_request("POST", "/blogs", blog_data)

    def update_blog(self, blog_id: int, blog_data: Dict) -> Dict:
        """Actualizar blog"""
        return self._make_request("PUT", f"/blogs/{blog_id}", blog_data)

    def add_comment(self, blog_id: int, comment_data: Dict) -> Dict:
        """Agregar comentario a blog"""
        return self._make_request("POST", f"/blogs/{blog_id}/comentarios", comment_data)

# Ejemplo de uso
if __name__ == "__main__":
    client = BlogClient()
    
    # Crear un blog
    blog_data = {
        "autor": {
            "nombres": "Juan Carlos",
            "apellidoPaterno": "Pérez",
            "apellidoMaterno": "García",
            "fechaNacimiento": "1990-05-15",
            "paisResidencia": "Bolivia",
            "correoElectronico": "juan.perez@email.com"
        },
        "nombre": "Mi Blog de Python",
        "tema": "Programación",
        "contenido": "Blog sobre programación en Python y mejores prácticas.",
        "periodicidad": "SEMANAL",
        "permiteComentarios": True
    }
    
    try:
        new_blog = client.create_blog(blog_data)
        print(f"Blog creado: {new_blog['nombre']}")
        
        # Agregar comentario
        comment_data = {
            "nombreComentarista": "Ana García",
            "correoComentarista": "ana@email.com",
            "paisResidenciaComentarista": "Bolivia",
            "contenido": "Excelente blog! Muy útil.",
            "puntuacion": 9
        }
        
        updated_blog = client.add_comment(new_blog['id'], comment_data)
        print(f"Comentario agregado al blog: {updated_blog['nombre']}")
        
    except Exception as e:
        print(f"Error: {e}")
```

#### Cliente con httpx (async)
```python
# async_blog_client.py
import httpx
import asyncio
from typing import Dict, List, Optional

class AsyncBlogClient:
    def __init__(self, base_url: str = "http://localhost:8861/api/v1"):
        self.base_url = base_url
        self.timeout = 10.0

    async def _make_request(self, method: str, endpoint: str, data: Optional[Dict] = None) -> Dict:
        url = f"{self.base_url}{endpoint}"
        headers = {"Content-Type": "application/json"}
        
        async with httpx.AsyncClient(timeout=self.timeout) as client:
            try:
                response = await client.request(
                    method=method,
                    url=url,
                    json=data,
                    headers=headers
                )
                response.raise_for_status()
                return response.json()
            except httpx.RequestError as e:
                raise Exception(f"API request failed: {e}")

    async def get_blogs(self) -> List[Dict]:
        return await self._make_request("GET", "/blogs")

    async def create_blog(self, blog_data: Dict) -> Dict:
        return await self._make_request("POST", "/blogs", blog_data)

# Ejemplo de uso async
async def main():
    client = AsyncBlogClient()
    
    # Obtener blogs de forma asíncrona
    blogs = await client.get_blogs()
    print(f"Encontrados {len(blogs)} blogs")
    
    for blog_data in blogs:
        blog = blog_data['blog']
        print(f"- {blog['nombre']} por {blog['autor']['nombres']}")

if __name__ == "__main__":
    asyncio.run(main())
```

### Java (Spring Boot/Microservicios)

#### Cliente REST con WebClient
```java
// BlogApiClient.java
@Service
public class BlogApiClient {
    
    private final WebClient webClient;
    private final String baseUrl = "http://localhost:8861/api/v1";
    
    public BlogApiClient() {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build();
    }
    
    public Mono<List<BlogResponse>> getBlogs() {
        return webClient.get()
            .uri("/blogs")
            .retrieve()
            .bodyToFlux(BlogResponse.class)
            .collectList();
    }
    
    public Mono<BlogResponse> getBlog(Long id) {
        return webClient.get()
            .uri("/blogs/{id}", id)
            .retrieve()
            .bodyToMono(BlogResponse.class);
    }
    
    public Mono<Blog> createBlog(CreateBlogRequest request) {
        return webClient.post()
            .uri("/blogs")
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Blog.class);
    }
    
    public Mono<Blog> addComment(Long blogId, CreateCommentRequest request) {
        return webClient.post()
            .uri("/blogs/{id}/comentarios", blogId)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(Blog.class);
    }
}

// DTOs
@Data
public class CreateBlogRequest {
    private Autor autor;
    private String nombre;
    private String tema;
    private String contenido;
    private Periodicidad periodicidad;
    private boolean permiteComentarios;
}

@Data
public class CreateCommentRequest {
    private String nombreComentarista;
    private String correoComentarista;
    private String paisResidenciaComentarista;
    private String contenido;
    private Integer puntuacion;
}
```

### PHP (Laravel/Symfony)

#### Cliente HTTP con Guzzle
```php
<?php
// BlogApiClient.php
class BlogApiClient
{
    private $client;
    private $baseUrl;
    
    public function __construct($baseUrl = 'http://localhost:8861/api/v1')
    {
        $this->baseUrl = $baseUrl;
        $this->client = new \GuzzleHttp\Client([
            'timeout' => 10,
            'headers' => [
                'Content-Type' => 'application/json',
                'Accept' => 'application/json'
            ]
        ]);
    }
    
    public function getBlogs()
    {
        try {
            $response = $this->client->get($this->baseUrl . '/blogs');
            return json_decode($response->getBody(), true);
        } catch (\Exception $e) {
            throw new \Exception("API request failed: " . $e->getMessage());
        }
    }
    
    public function createBlog($blogData)
    {
        try {
            $response = $this->client->post($this->baseUrl . '/blogs', [
                'json' => $blogData
            ]);
            return json_decode($response->getBody(), true);
        } catch (\Exception $e) {
            throw new \Exception("API request failed: " . $e->getMessage());
        }
    }
    
    public function addComment($blogId, $commentData)
    {
        try {
            $response = $this->client->post($this->baseUrl . "/blogs/{$blogId}/comentarios", [
                'json' => $commentData
            ]);
            return json_decode($response->getBody(), true);
        } catch (\Exception $e) {
            throw new \Exception("API request failed: " . $e->getMessage());
        }
    }
}

// Ejemplo de uso
$client = new BlogApiClient();

$blogData = [
    'autor' => [
        'nombres' => 'María Elena',
        'apellidoPaterno' => 'Rodríguez',
        'apellidoMaterno' => 'González',
        'fechaNacimiento' => '1985-03-15',
        'paisResidencia' => 'Bolivia',
        'correoElectronico' => 'maria.rodriguez@email.com'
    ],
    'nombre' => 'Mi Blog de PHP',
    'tema' => 'Desarrollo Web',
    'contenido' => 'Blog sobre desarrollo web con PHP y Laravel.',
    'periodicidad' => 'SEMANAL',
    'permiteComentarios' => true
];

try {
    $newBlog = $client->createBlog($blogData);
    echo "Blog creado: " . $newBlog['nombre'] . "\n";
} catch (Exception $e) {
    echo "Error: " . $e->getMessage() . "\n";
}
?>
```

## 🔧 Configuración y Mejores Prácticas

### Configuración de Timeout
```javascript
// Configuración recomendada por tipo de operación
const TIMEOUTS = {
  GET: 5000,      // 5 segundos para lecturas
  POST: 10000,    // 10 segundos para escrituras
  PUT: 10000,     // 10 segundos para actualizaciones
  DELETE: 5000    // 5 segundos para eliminaciones
};
```

### Manejo de Errores Robusto
```javascript
// utils/errorHandler.js
export const handleApiError = (error) => {
  if (error.status === 400) {
    // Errores de validación
    if (error.details.validationErrors) {
      return {
        type: 'validation',
        message: 'Datos inválidos',
        errors: error.details.validationErrors
      };
    }
    return {
      type: 'business',
      message: error.details.message || 'Error de lógica de negocio'
    };
  }
  
  if (error.status === 404) {
    return {
      type: 'not_found',
      message: 'Recurso no encontrado'
    };
  }
  
  if (error.status >= 500) {
    return {
      type: 'server',
      message: 'Error interno del servidor'
    };
  }
  
  return {
    type: 'unknown',
    message: 'Error desconocido'
  };
};
```

### Retry Logic
```javascript
// utils/retry.js
export const withRetry = async (fn, maxRetries = 3, delay = 1000) => {
  for (let i = 0; i < maxRetries; i++) {
    try {
      return await fn();
    } catch (error) {
      if (i === maxRetries - 1) throw error;
      
      // Solo reintentar en errores de red o servidor
      if (error.status >= 500 || error.status === 0) {
        await new Promise(resolve => setTimeout(resolve, delay * Math.pow(2, i)));
        continue;
      }
      
      throw error;
    }
  }
};
```

### Caché de Datos
```javascript
// utils/cache.js
class ApiCache {
  constructor(ttl = 300000) { // 5 minutos por defecto
    this.cache = new Map();
    this.ttl = ttl;
  }
  
  set(key, data) {
    this.cache.set(key, {
      data,
      timestamp: Date.now()
    });
  }
  
  get(key) {
    const item = this.cache.get(key);
    if (!item) return null;
    
    if (Date.now() - item.timestamp > this.ttl) {
      this.cache.delete(key);
      return null;
    }
    
    return item.data;
  }
  
  clear() {
    this.cache.clear();
  }
}

// Uso en servicio
const cache = new ApiCache();

export const getBlogsWithCache = async () => {
  const cacheKey = 'blogs';
  const cached = cache.get(cacheKey);
  
  if (cached) {
    return cached;
  }
  
  const data = await blogService.getBlogs();
  cache.set(cacheKey, data);
  return data;
};
```

## 🧪 Testing de Integración

### Tests Unitarios
```javascript
// tests/blogService.test.js
import { blogService } from '../services/blogService';

// Mock de fetch
global.fetch = jest.fn();

describe('BlogService', () => {
  beforeEach(() => {
    fetch.mockClear();
  });
  
  test('should create blog successfully', async () => {
    const mockBlog = { id: 1, nombre: 'Test Blog' };
    fetch.mockResolvedValueOnce({
      ok: true,
      json: async () => mockBlog
    });
    
    const result = await blogService.createBlog({
      nombre: 'Test Blog',
      // ... otros campos
    });
    
    expect(result).toEqual(mockBlog);
    expect(fetch).toHaveBeenCalledWith(
      expect.stringContaining('/blogs'),
      expect.objectContaining({
        method: 'POST',
        body: expect.any(String)
      })
    );
  });
  
  test('should handle API errors', async () => {
    fetch.mockResolvedValueOnce({
      ok: false,
      status: 400,
      json: async () => ({ message: 'Validation error' })
    });
    
    await expect(blogService.createBlog({})).rejects.toThrow('Validation error');
  });
});
```

### Tests de Integración
```javascript
// tests/integration.test.js
import { blogService } from '../services/blogService';

describe('Blog API Integration', () => {
  test('should create blog and add comment', async () => {
    // Crear blog
    const blogData = {
      autor: {
        nombres: 'Test User',
        apellidoPaterno: 'Test',
        fechaNacimiento: '1990-01-01',
        paisResidencia: 'Bolivia',
        correoElectronico: 'test@example.com'
      },
      nombre: 'Integration Test Blog',
      tema: 'Testing',
      contenido: 'This is a test blog for integration testing.',
      periodicidad: 'SEMANAL',
      permiteComentarios: true
    };
    
    const blog = await blogService.createBlog(blogData);
    expect(blog.id).toBeDefined();
    expect(blog.nombre).toBe('Integration Test Blog');
    
    // Agregar comentario
    const commentData = {
      nombreComentarista: 'Test Commenter',
      correoComentarista: 'commenter@example.com',
      paisResidenciaComentarista: 'Bolivia',
      contenido: 'Great blog!',
      puntuacion: 9
    };
    
    const updatedBlog = await blogService.addComment(blog.id, commentData);
    expect(updatedBlog.comentarios).toHaveLength(1);
    expect(updatedBlog.comentarios[0].contenido).toBe('Great blog!');
  });
});
```

## 📊 Monitoreo y Observabilidad

### Métricas de API
```javascript
// utils/metrics.js
class ApiMetrics {
  constructor() {
    this.metrics = {
      requests: 0,
      errors: 0,
      responseTime: []
    };
  }
  
  recordRequest(duration, success) {
    this.metrics.requests++;
    this.metrics.responseTime.push(duration);
    
    if (!success) {
      this.metrics.errors++;
    }
  }
  
  getStats() {
    const avgResponseTime = this.metrics.responseTime.reduce((a, b) => a + b, 0) / this.metrics.responseTime.length;
    const errorRate = this.metrics.errors / this.metrics.requests;
    
    return {
      totalRequests: this.metrics.requests,
      errorRate: errorRate,
      averageResponseTime: avgResponseTime
    };
  }
}

// Uso en interceptor
const metrics = new ApiMetrics();

export const withMetrics = (fn) => {
  return async (...args) => {
    const start = Date.now();
    try {
      const result = await fn(...args);
      metrics.recordRequest(Date.now() - start, true);
      return result;
    } catch (error) {
      metrics.recordRequest(Date.now() - start, false);
      throw error;
    }
  };
};
```

## 🚀 Despliegue y Configuración

### Variables de Entorno
```bash
# .env
BLOG_API_URL=http://localhost:8861/api/v1
BLOG_API_TIMEOUT=10000
BLOG_API_RETRY_ATTEMPTS=3
BLOG_API_CACHE_TTL=300000
```

### Configuración de Producción
```javascript
// config/production.js
export const productionConfig = {
  apiUrl: process.env.BLOG_API_URL || 'https://api.tudominio.com/api/v1',
  timeout: parseInt(process.env.BLOG_API_TIMEOUT) || 10000,
  retryAttempts: parseInt(process.env.BLOG_API_RETRY_ATTEMPTS) || 3,
  cacheTtl: parseInt(process.env.BLOG_API_CACHE_TTL) || 300000,
  enableMetrics: true,
  enableLogging: true
};
```
