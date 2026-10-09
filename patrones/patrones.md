# Reto Prinplup: Patrones de Diseño v2

En AquaPort v2, abandonamos los largos `if/else` y las dependencias acopladas para dar paso a un diseño elegante basado en tres Patrones de Diseño clave (GoF).

---

## 1. Strategy
**¿Qué hace?**
Permite definir una familia de algoritmos, encapsular cada uno y hacerlos intercambiables en tiempo de ejecución. Permite que el algoritmo varíe independientemente de los clientes que lo utilizan.

**¿Por qué y cómo se implementó?**
Se implementó porque la lógica para elegir qué drone asignar puede variar según el contexto operativo (necesitar el de más batería, el más cercano, o el de mayor carga para misiones críticas).
- **Interfaz:** `EstrategiaAsignacion` define el contrato `seleccionar(List<DroneAcuatico> disponibles)`.
- **Estrategias (Algoritmos):** `MayorBateriaStrategy`, `ZonaCercanaStrategy`, `PrioridadCriticaStrategy`.
- **Contexto:** `AsignadorAutomatico` tiene un atributo de tipo `EstrategiaAsignacion` y delega la selección. Así, no tenemos un `if (estrategia instanceof...)` (lo cual sería un antipatrón), simplemente llamamos a `estrategia.seleccionar(flota)`.

---

## 2. Observer
**¿Qué hace?**
Define una dependencia uno-a-muchos entre objetos, de manera que cuando uno cambia su estado, todos sus dependientes son notificados y actualizados automáticamente.

**¿Por qué y cómo se implementó?**
Se implementó para la gestión de alertas, ya que diferentes partes del sistema necesitan reaccionar cuando un drone falla, sin que la clase del drone deba conocer a estos interesados.
- **Sujeto Observable:** `AsignadorAutomatico` mantiene una lista interna de `observadores` (`List<DroneObserver>`). Si un drone en `FALLO` intenta asignarse (o reporta fallo), itera sobre la lista y llama a `onDroneFallo()`.
- **Interfaz Observer:** `DroneObserver`.
- **Observadores (Listeners):** `CentroControlObserver` y `TecnicoMantenimientoObserver`. Se pueden suscribir/desuscribir en tiempo de ejecución.

---

## 3. Factory Method
**¿Qué hace?**
Define una interfaz para crear un objeto, pero deja que las subclases decidan qué clase instanciar. Delega la creación de instancias a subclases/métodos especializados.

**¿Por qué y cómo se implementó?**
Se implementó porque ahora AquaPort v2 maneja múltiples modelos de drones (`Superficial`, `Semisumergido`, `Buceador`) que heredan de una misma abstracción base.
- **Abstracción Base:** `DroneAcuatico` (convertido de `record` a `abstract class`).
- **Creador:** `DroneFactory`, que evalúa un `String tipo` (o Enum) y retorna la subclase específica ya configurada con sus límites de carga (`capacidadCargaMax`).
- De esta forma, si la Universidad compra un cuarto tipo de drone mañana, la lógica central de AquaPort permanece intacta, solo se añade el caso en el Factory.
