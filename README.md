# AssignTasks

**AssignTasks** es una aplicación web para la gestión de tareas con funcionalidades para líderes y trabajadores. Utiliza **Spring Boot** (Java) en el backend, **Vite + React** en el frontend, y **Firebase** para autenticación y WebSockets para comunicación en tiempo real.


## Requisitos

- Java 17 o superior
- Maven
- Node.js y npm
- Archivo `firebase-config.json` (credenciales de Firebase)

## Instrucciones para ejecutar el proyecto

### 1. Backend

#### Estructura esperada

El archivo de configuración de Firebase debe estar en:
backend/src/main/resources/firebase/firebase-config.json


#### Comandos para iniciar el backend

Desde la raíz del proyecto:

```bash
cd backend
mvn clean
mvn install
mvn spring-boot:run
```
Esto ejecutará el servidor en http://localhost:8080.

### 2. Frontend

Desde la raíz del proyecto:

```bash
cd frontend/frontend
npm install
npm run dev
```
Esto iniciará la aplicación en http://localhost:5173.

### Equipo

- Julián Enrique Espinoza Valenzuela | A01254679
- Santiago Gutiérrez González | A00572499
- Alejandro Moncada Espinosa | A01638343
- Ana Camila Jimenez Mendoza | A01174422
- Jorge Ivan Sanchez Gonzalez | A01761414

