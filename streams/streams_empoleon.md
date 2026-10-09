# Reto Empoleon: AquaPort Enterprise v3 - Streams & Lambdas

En la versión Enterprise (v3), AquaPort gestiona 60 drones repartidos en 4 zonas hídricas. Las misiones ahora son de tipo "multi-etapa", atravesando múltiples `Waypoints`, y se requiere analítica avanzada para el rendimiento de la flota y eficiencia de las zonas.

## 1. Eficiencia por Zona

Para calcular la eficiencia de cada zona (misiones completadas / misiones totales), agrupamos las misiones según su punto de partida y usamos el colector `collectingAndThen` para transformar la lista resultante en un porcentaje de eficiencia:

```java
public static Map<String, Double> calcularEficienciaPorZona(List<Mision> misiones) {
    return misiones.stream()
        .collect(Collectors.groupingBy(
            Mision::getPuntoPartida,
            Collectors.collectingAndThen(
                Collectors.toList(),
                lista -> {
                    long total = lista.size();
                    long completadas = lista.stream()
                        .filter(m -> m.getEstado() == EstadoMision.COMPLETADA)
                        .count();
                    return total == 0 ? 0.0 : (double) completadas / total;
                }
            )
        ));
}
```

## 2. Dron con Mejor Historial

Se busca de forma fluida el dron con la mayor cantidad de entregas exitosas:

```java
public static Optional<DroneAcuatico> droneMasExitoso(List<DroneAcuatico> flota) {
    return flota.stream()
        .max(Comparator.comparingInt(DroneAcuatico::getEntregasExitosas));
}
```

## 3. Planificar Ruta Óptima (Multi-etapa)

Por cada `Waypoint` de la ruta, el sistema selecciona al instante el mejor dron que se encuentre en la zona de dicho waypoint y que esté disponible. Si una zona no tiene drones aptos, arroja un fallo temprano de orquestación.

```java
public static List<DroneAcuatico> planificarRutaOptima(List<Waypoint> ruta, List<DroneAcuatico> flota) {
    return ruta.stream()
        .map(wp -> flota.stream()
            .filter(d -> d.disponible() && d.getZona().equals(wp.getZona()))
            .max(Comparator.comparingInt(DroneAcuatico::getBateria))
            .orElseThrow(() -> new IllegalStateException("No hay drones en " + wp.getZona()))
        )
        .collect(Collectors.toList());
}
```

## 4. Collector Personalizado de Rendimiento

Para evitar dobles pasadas (reducir CPU overhead), se implementó un `Collector.of()` manual que agrupa por zona y, en el mismo barrido de acumulación mutante, calcula el promedio de batería usando un arreglo primitivo como estado acumulador:

```java
public static Map<String, Double> bateriaPromedioPorZonaCustom(List<DroneAcuatico> flota) {
    return flota.stream()
        .collect(Collectors.groupingBy(
            DroneAcuatico::getZona,
            Collector.of(
                () -> new double[2], // [suma, cantidad]
                (acc, d) -> { acc[0] += d.getBateria(); acc[1]++; },
                (acc1, acc2) -> { acc1[0] += acc2[0]; acc1[1] += acc2[1]; return acc1; },
                acc -> acc[1] == 0 ? 0.0 : acc[0] / acc[1]
            )
        ));
}
```

## 💡 Justificación de Hilos (Sequential vs Parallel Streams)

1. **Eficiencia y Dron Exitoso (Sequential Stream):** Buscar un máximo o iterar sobre listas transaccionales pequeñas a medianas genera más *overhead* de coordinación (Fork/Join framework) que el tiempo que ahorra la concurrencia. No aplica el paralelismo aquí, el sequential stream ganará por su predictibilidad y caché de CPU.
2. **Planificación Multi-Etapa (Parallel Stream Opcional):** Si la ruta multi-etapa se vuelve masiva o el cálculo de filtro de drone (cálculos matemáticos densos como heurística Haversine para distancias geo-espaciales reales entre drone y waypoint) fuese muy complejo y tardado por cada elemento, **aquí sí se justificaría invocar `ruta.parallelStream()`**. Al particionar la búsqueda de tramos en distintos cores, la red neuronal de asignación arrojará la lista orquestada en una fracción del tiempo de reloj, haciendo valer el costo del *Thread Dispatching*.
