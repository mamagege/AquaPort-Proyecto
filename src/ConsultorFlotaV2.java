import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Comparator;
import java.util.stream.Collectors;

public class ConsultorFlotaV2 {

    /**
     * 1) Agrupar drones disponibles por tipo.
     */
    public Map<String, List<DroneAcuatico>> agruparDronesDisponiblesPorTipo(List<DroneAcuatico> flota) {
        return flota.stream()
                .filter(DroneAcuatico::disponible)
                .collect(Collectors.groupingBy(DroneAcuatico::getTipo));
    }

    /**
     * 2) Obtener el drone óptimo para una zona dada (mayor batería, tipo compatible).
     * Nota: Para simplificar, asumimos que todos los tipos podrían ser compatibles si coinciden con un "tipoNecesario".
     */
    public Optional<DroneAcuatico> obtenerDroneOptimo(List<DroneAcuatico> flota, String zonaDestino, String tipoNecesario) {
        return flota.stream()
                .filter(DroneAcuatico::disponible)
                .filter(d -> d.getTipo().equalsIgnoreCase(tipoNecesario))
                // Podríamos filtrar por cercanía a la zona, pero usaremos batería como principal criterio de optimización.
                .max(Comparator.comparingInt(DroneAcuatico::getBateria));
    }

    /**
     * 3) Calcular el promedio de batería por tipo de drone.
     */
    public Map<String, Double> promedioBateriaPorTipo(List<DroneAcuatico> flota) {
        return flota.stream()
                .collect(Collectors.groupingBy(
                        DroneAcuatico::getTipo,
                        Collectors.averagingInt(DroneAcuatico::getBateria)
                ));
    }

    /**
     * 4) Separar misiones CRÍTICAS de las demás usando partitioningBy.
     * Retorna un Map donde `true` son misiones CRÍTICAS y `false` son las demás.
     */
    public Map<Boolean, List<Mision>> separarMisionesCriticas(List<Mision> misiones) {
        return misiones.stream()
                .collect(Collectors.partitioningBy(m -> m.getPrioridad() == Prioridad.CRITICA));
    }

    /**
     * 5) Listar todas las zonas únicas que cubre la flota activa (disponible o en misión).
     */
    public Set<String> obtenerZonasUnicasActivas(List<DroneAcuatico> flota) {
        return flota.stream()
                .filter(d -> d.getEstado() == EstadoDrone.DISPONIBLE || d.getEstado() == EstadoDrone.EN_MISION)
                .map(DroneAcuatico::getZona)
                .collect(Collectors.toSet());
    }
}
