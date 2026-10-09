# AquaPort

Sistema integral de gestión y monitoreo de la flota de drones acuáticos.


## Auditoría de Principios SOLID en AquaPort v2

Durante la evolución a la versión v2 del sistema, se estructuró la arquitectura siguiendo rigurosamente los principios SOLID para garantizar el fácil mantenimiento y escalabilidad de la flota de drones.

### 1. S - Single Responsibility Principle (SRP)
**Aplicado en:** `ConsultorFlotaV2`, `ValidadorMision`, `DroneFactory`.
**Por qué importa:** Cada clase tiene una única razón para cambiar. Si la lógica de filtrado por *streams* cambia, solo se altera `ConsultorFlotaV2`. Si cambia cómo se valida la batería de un drone, solo se altera `ValidadorMision`. Si cambia el proceso de instanciación de un drone, solo se altera el Factory. En AquaPort esto previene que un cambio en las métricas rompa la lógica de validación de rutas.

### 2. O - Open/Closed Principle (OCP)
**Aplicado en:** Jerarquía de `DroneAcuatico`, `EstrategiaAsignacion`.
**Por qué importa:** El código está abierto a la extensión pero cerrado a la modificación. Al introducir un hipotético "DroneSumergibleProfundo", solo se crea una nueva clase hija y se agrega una línea en el `DroneFactory`. Ni el `AsignadorAutomatico`, ni el `ValidadorMision`, ni ninguna estrategia requiere ser editada, reduciendo a cero el riesgo de romper funcionalidades existentes (las pruebas unitarias no se tocan).

### 3. L - Liskov Substitution Principle (LSP)
**Aplicado en:** `DroneSuperficial`, `DroneSemisumergido`, `DroneBuceador`.
**Por qué importa:** Cualquier clase derivada de `DroneAcuatico` debe poder usarse como si fuera la clase base sin romper el comportamiento esperado. Ninguno de nuestros drones arroja un `UnsupportedOperationException` para los métodos de consulta de `getBateria()` o `disponible()`. El `ValidadorMision` los procesa a todos como simples `DroneAcuatico` y la aplicación opera perfectamente.

### 4. I - Interface Segregation Principle (ISP)
**Aplicado en:** `DroneObserver`, `EstrategiaAsignacion`.
**Por qué importa:** No obligamos a las clases a depender de interfaces que no usan. `EstrategiaAsignacion` tiene un único y simple método `seleccionar(...)`. De la misma forma, un observador (`CentroControlObserver`) solo debe implementar `onDroneFallo(...)`. Los desarrolladores de nuevos módulos en AquaPort no están obligados a cargar con firmas de métodos "basura" o innecesarios.

### 5. D - Dependency Inversion Principle (DIP)
**Aplicado en:** `AsignadorAutomatico`.
**Por qué importa:** Los módulos de alto nivel no deben depender de módulos de bajo nivel; ambos deben depender de abstracciones. `AsignadorAutomatico` no depende de `MayorBateriaStrategy`, depende de la abstracción `EstrategiaAsignacion`. Esto permite que el centro de control inyecte en tiempo de ejecución la estrategia dinámica deseada. De la misma forma, el asignador alerta sobre incidentes a la interfaz `DroneObserver`, sin saber si es un SMS, un log, o un técnico físico, desacoplando completamente las reglas de negocio de la infraestructura.
