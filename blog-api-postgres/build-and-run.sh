#!/bin/bash

# Script para construir y ejecutar la aplicación Blog API

echo "🚀 Iniciando construcción y despliegue de Blog API..."

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para imprimir mensajes con color
print_message() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Verificar que Maven esté instalado
if ! command -v mvn &> /dev/null; then
    print_error "Maven no está instalado. Por favor instala Maven primero."
    exit 1
fi

# Verificar que Docker esté instalado
if ! command -v docker &> /dev/null; then
    print_error "Docker no está instalado. Por favor instala Docker primero."
    exit 1
fi

print_message "Limpiando y compilando el proyecto..."
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    print_success "Compilación exitosa"
else
    print_error "Error en la compilación"
    exit 1
fi

print_message "Construyendo imagen Docker..."
docker build -t blog-api:latest .

if [ $? -eq 0 ]; then
    print_success "Imagen Docker construida exitosamente"
else
    print_error "Error al construir la imagen Docker"
    exit 1
fi

print_message "Deteniendo contenedores existentes..."
docker-compose down

print_message "Iniciando servicios con Docker Compose..."
docker-compose up -d

if [ $? -eq 0 ]; then
    print_success "Servicios iniciados exitosamente"
    echo ""
    print_message "🌐 La aplicación está disponible en:"
    echo "   - API: http://localhost:8861"
    echo "   - Swagger UI: http://localhost:8861/swagger-ui.html"
    echo "   - PostgreSQL: localhost:5432"
    echo ""
    print_message "Para ver los logs:"
    echo "   docker-compose logs -f blog-api"
    echo ""
    print_message "🛑 Para detener los servicios:"
    echo "   docker-compose down"
else
    print_error "Error al iniciar los servicios"
    exit 1
fi
