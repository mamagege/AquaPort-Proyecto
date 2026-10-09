import java.util.List;
import java.util.Optional;

public class ZonaCercanaStrategy implements EstrategiaAsignacion {
    @Override
    public Optional<DroneAcuatico> seleccionar(List<DroneAcuatico> disponibles) {
        // Implementación simplificada: retorna el primero disponible. 
        // En la vida real, calcularía la distancia geográfica (Haversine).
        return disponibles.stream()
                .filter(DroneAcuatico::disponible)
                .findFirst();
    }
}
