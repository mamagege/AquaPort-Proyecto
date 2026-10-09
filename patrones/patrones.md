# Reto Empoleon: Integración de Patrones de Diseño GOF

En la escala Enterprise (v3), un sistema rígido fracasa rápido. Hemos inyectado dinamismo a AquaPort adoptando **3 Patrones de Diseño Estructurales y de Comportamiento** que interactúan de forma limpia, cumpliendo los principios SOLID.

## 1. Chain of Responsibility (Cadena de Responsabilidad)
La asignación de misiones antes dependía de un bloque denso de `if/else`. Ahora, implementamos una canalización (pipeline) donde la solicitud viaja a través de distintos eslabones validadores. Si uno falla, la cadena se rompe de forma controlada (Early Exit), ahorrando cómputo.

- **Componentes:** `ValidadorCadena` (Abstracto), `ValidadorBateria`, `ValidadorCapacidadCarga`, `ValidadorZonaActiva` y `ValidadorCondicionesHidricas`.
- **Ventaja Enterprise:** Si en el futuro llega un "ValidadorDeSeguros", simplemente se añade con `.setSiguiente(nuevoValidador)` sin alterar ni una línea del asignador principal (Open/Closed Principle).
- **Pruebas (Mockito):** Comprobamos mediante `verify(valCarga, never()).validar(...)` que, cuando la batería falla, el pipeline aborta, previniendo que los eslabones siguientes se ejecuten.

## 2. Decorator (Decorador)
Queríamos agregar Telemetría y Cifrado Militar a los drones. Si usamos herencia clásica, tendríamos la explosión de clases: `DroneBuceadorConCifrado`, `DroneBuceadorConTelemetria`, `DroneBuceadorConAmbas`, etc.

- **Solución:** Creamos `DroneDecorator` que envuelve al `DroneAcuatico` base.
- **Componentes:** `DroneConMonitoreo` y `DroneConCifrado`.
- **Implementación:** `DroneConCifrado(DroneConMonitoreo(droneBase))`. Cada capa añade su lógica (`cifradoActivo = true`) antes o después de delegar (`super.registrarTelemetria()`).
- **Ventaja Enterprise:** Dinámicamente en tiempo de ejecución, a un Drone "le ponemos la mochila" de Telemetría sin tocar el código fuente del drone.

## 3. Adapter (Adaptador)
El Centro Meteorológico nos entregaba datos crudos, en inglés y en formato JSON (`{"waterLevel": 85.5, "turbidity": 12.0}`). Que nuestro núcleo de dominio sepa "qué es un JSON" violaba la Clean Architecture (Ports & Adapters).

- **Solución:** `AdaptadorAPIHidrica` actúa como un enchufe o traductor.
- **Componentes:** Implementa (o inyecta) el proveedor `APIHidricaExterna` y retorna nuestro modelo puro `CondicionesHidricas` (`nivelAgua` y `turbidez`).
- **Pruebas (Mockito):** Mockeamos la respuesta límite del API para simular lecturas crudas. El Adaptador las convierte y el Validador de la Cadena evalúa las reglas de negocio sobre el objeto adaptado.

*(Todas las implementaciones incluyen cobertura total avalada por JaCoCo).*
