import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.stream.Collector;

public class EmpoleonStreams {

    // 1. Calcular la eficiencia de cada zona (misiones completadas / misiones totales)
    public static Map<String, Double> calcularEficienciaPorZona(List<Mision> misiones) {
        return misiones.stream()
            .collect(Collectors.groupingBy(
                Mision::getPuntoPartida, // Suponiendo que la eficiencia se mide por zona de origen
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

    // 2. Encontrar el drone con el mejor historial de entregas exitosas
    public static Optional<DroneAcuatico> droneMasExitoso(List<DroneAcuatico> flota) {
        // Al tratarse de un simple máximo en una colección que no superará los miles de elementos,
        // usar parallelStream() generaría más overhead en la coordinación de hilos que ganancia en tiempo real.
        // Se usa stream secuencial por eficiencia.
        return flota.stream()
            .max(Comparator.comparingInt(DroneAcuatico::getEntregasExitosas));
    }

    // 3. Planificar la ruta óptima seleccionando el drone disponible más cercano (mayor batería en esta zona simulada)
    public static List<DroneAcuatico> planificarRutaOptima(List<Waypoint> ruta, List<DroneAcuatico> flota) {
        // Por cada waypoint intermedio (tramo), buscar el drone de esa zona que esté disponible y tenga mayor batería.
        // Si hay una gran cantidad de waypoints y drones, y la búsqueda requiere cálculos de distancia pesados,
        // aquí sí justificaría un parallelStream() en las zonas para paralelizar la búsqueda del drone óptimo.
        return ruta.stream()
            .map(wp -> flota.stream()
                .filter(d -> d.disponible() && d.getZona().equals(wp.getZona()))
                .max(Comparator.comparingInt(DroneAcuatico::getBateria))
                .orElseThrow(() -> new IllegalStateException("No hay drones disponibles para el waypoint en " + wp.getZona()))
            )
            .collect(Collectors.toList());
    }

    // 4. Collector personalizado que agrupe drones por zona y calcule la batería promedio por zona en una sola pasada
    public static Map<String, Double> bateriaPromedioPorZonaCustom(List<DroneAcuatico> flota) {
        return flota.stream()
            .collect(Collectors.groupingBy(
                DroneAcuatico::getZona,
                Collector.of(
                    () -> new double[2], // [0] = suma, [1] = cantidad
                    (acc, d) -> { acc[0] += d.getBateria(); acc[1]++; },
                    (acc1, acc2) -> { acc1[0] += acc2[0]; acc1[1] += acc2[1]; return acc1; },
                    acc -> acc[1] == 0 ? 0.0 : acc[0] / acc[1]
                )
            ));
    }
}
