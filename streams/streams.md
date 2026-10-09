# Reto Prinplup: AquaPort v2 - Streams & Lambdas

Con la expansión de la flota a 15 drones de 3 tipos especializados (Superficial, Semisumergido, Buceador), las necesidades operativas de AquaPort han crecido exponencialmente. La versión v2 deja atrás la asignación manual para introducir algoritmos de asignación automática activa, basados puramente en Streams de Java.

## Nuevas Estructuras de Dominio
1. **DroneAcuatico (Abstracto):** Ahora cuenta con 3 clases hijas (`DroneSuperficial`, `DroneSemisumergido`, `DroneBuceador`) centralizadas mediante un patrón *Factory Method* en `DroneFactory`.
2. **EstadoDrone:** Enum con estados operativos reales (DISPONIBLE, EN_MISION, RECARGANDO, MANTENIMIENTO, SUMERGIDO).
3. **Prioridad:** Nivel de criticidad para orquestar la atención (CRITICA, ALTA, NORMAL, BAJA).

## Operaciones Funcionales Implementadas (`ConsultorFlotaV2.java`)

1. **Agrupación por Tipo:**
   Utilizando `Collectors.groupingBy(DroneAcuatico::getTipo)` dividimos la flota disponible en un `Map<String, List<DroneAcuatico>>`. Fundamental para balances de carga.

2. **Dron Óptimo por Batería:**
   Mediante `.filter()` descartamos los incompatibles y no disponibles, y a través de `.max(Comparator.comparingInt(DroneAcuatico::getBateria))` extraemos el dron con más autonomía restante.

3. **Promedios de Batería:**
   Combinamos `groupingBy` con un colector intermedio `Collectors.averagingInt(DroneAcuatico::getBateria)` para monitorear el desgaste de cada categoría de la flota en tiempo real.

4. **Partición de Emergencias (CRITICA vs RESTO):**
   Con `Collectors.partitioningBy(m -> m.getPrioridad() == Prioridad.CRITICA)` generamos dos universos de misiones (True/False). Esto garantiza que el hilo de ejecución principal siempre atienda a los `True` primero sin requerir ordenamiento de listas costoso.

5. **Listado de Zonas de Cobertura Activas:**
   Filtramos los drones operativos, mapeamos a su respectiva zona con `.map(DroneAcuatico::getZona)` y finalmente recolectamos con `Collectors.toSet()` para eliminar automáticamente los duplicados y obtener las zonas geográficas abarcadas en este instante.
