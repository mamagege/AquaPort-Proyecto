# Reto Empoleon: Auditoría SOLID y Clean Architecture

Hemos asegurado que el corazón de AquaPort (el **Dominio**) no se contamine con implementaciones externas. Siguiendo la *Clean Architecture* y el principio de Inversión de Dependencias (DIP) de SOLID, hemos aplicado una estricta revisión.

## 1. Revisión de Imports
Auditoría manual y vía escáner de SonarQube confirma que **ningún archivo dentro del dominio** (los paquetes/clases principales de lógica de negocio) posee un import de librerías externas como `Mockito`, `Jackson`, `HttpClient` o framework alguno.

```java
// Las únicas dependencias que permite nuestro dominio:
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
```

## 2. Inyección de Dependencias
Se ha comprobado que ningún servicio o caso de uso hace un `new ClaseInfraestructura()`. Todas las dependencias se inyectan a través del constructor, solicitando Interfaces y no clases concretas.

*Ejemplo (ValidadorCondicionesHidricas):*
```java
// BIEN: Recibe la abstracción (Puerto)
public ValidadorCondicionesHidricas(ProveedorCondiciones proveedor) {
    this.proveedor = proveedor;
}

// MAL: No hace "new AdaptadorAPIHidrica()"
```

## 3. Mapa de Dependencias entre Capas (Ports & Adapters)

La arquitectura sigue una topología de capas concéntricas o hexágono, donde **la capa exterior siempre apunta hacia la interior, y nunca al revés**.



**Reglas aplicadas:**
1. El **Dominio** define la interfaz `ProveedorCondiciones` (El Puerto).
2. El **Dominio** (`ValidadorCondicionesHidricas`) utiliza ese puerto sin importarle quién o cómo lo implementa.
3. La **Infraestructura** (`AdaptadorAPIHidrica`) implementa el puerto y contiene los imports feos (JSON, HTTP, etc) para hablar con la API real.

Este aislamiento garantiza que si mañana cambiamos la API meteorológica por un sensor físico, la lógica de validación de los drones no sufrirá ninguna modificación (Open/Closed Principle).
