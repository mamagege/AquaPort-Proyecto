import java.util.List;
import java.util.Optional;
import java.util.Comparator;

public class PrioridadCriticaStrategy implements EstrategiaSeleccion {
    @Override
    public Optional<DroneAcuatico> seleccionar(List<DroneAcuatico> disponibles, Mision mision) {
        // En prioridad crítica, se escoge el drone con mayor capacidad de carga
        // para asegurar que pueda soportar cualquier equipo necesario.
        return disponibles.stream()
                .filter(DroneAcuatico::disponible)
                .max(Comparator.comparingDouble(DroneAcuatico::getCapacidadCargaMax));
    }
}
