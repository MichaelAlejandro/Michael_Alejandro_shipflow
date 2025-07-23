# 📦 ShipFlow

**ShipFlow** es un microservicio monolítico en Kotlin con Spring Boot, PostgreSQL y JPA. Permite registrar paquetes, cambiar su estado mediante eventos y consultar el historial de cada envío.

---

## 🚀 Tecnologías utilizadas

- Kotlin + Spring Boot
- PostgreSQL (Docker)
- Spring Data JPA
- Gradle
- Postman (para pruebas)

---

## 🧪 Endpoints REST

| Método | Ruta                                      | Descripción                           |
|--------|-------------------------------------------|----------------------------------------|
| POST   | `/api/packages`                           | Crear nuevo paquete                    |
| GET    | `/api/packages`                           | Listar todos los paquetes              |
| GET    | `/api/packages/{trackingId}`              | Consultar paquete por trackingId       |
| PUT    | `/api/packages/{trackingId}/status`       | Cambiar estado del paquete             |
| GET    | `/api/packages/{trackingId}/events`       | Ver historial de eventos del paquete   |

---

## ✅ Reglas de negocio

- ❌ Origen y destino no pueden ser iguales
- ❌ Descripción ≤ 50 caracteres
- ✅ Estados válidos: `PENDING`, `IN_TRANSIT`, `DELIVERED`, `ON_HOLD`, `CANCELLED`
- 🔄 Transiciones validadas (ej: `DELIVERED` solo tras `IN_TRANSIT`)

---

## ▶️ Cómo ejecutar

### 1. Clonar el repositorio
```bash
git clone https://github.com/tuusuario/alejandro_michael_shipflow.git
cd alejandro_michael_shipflow
```

### 2. Levantar PostgreSQL
```bash
docker-compose up -d
```

### 3. Ejecutar el proyecto
```bash
./gradlew bootRun
```

### 4. Probar con Postman
Importa el archivo `Shipflow.postman_collection.json` y usa los endpoints.
