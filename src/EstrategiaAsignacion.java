import java.util.List;
import java.util.Optional;

public interface EstrategiaAsignacion {
    Optional<DroneAcuatico> seleccionar(List<DroneAcuatico> disponibles);
}
