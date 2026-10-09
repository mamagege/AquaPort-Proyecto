import java.util.List;
import java.util.Optional;
import java.util.Comparator;

public class MayorBateriaStrategy implements EstrategiaAsignacion {
    @Override
    public Optional<DroneAcuatico> seleccionar(List<DroneAcuatico> disponibles) {
        return disponibles.stream()
                .filter(DroneAcuatico::disponible)
                .max(Comparator.comparingInt(DroneAcuatico::getBateria));
    }
}
