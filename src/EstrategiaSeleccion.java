import java.util.List;
import java.util.Optional;

public interface EstrategiaSeleccion {
    Optional<DroneAcuatico> seleccionar(List<DroneAcuatico> disponibles, Mision mision);
}
