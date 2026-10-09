import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AsignadorAutomatico {
    private final EstrategiaSeleccion estrategia;
    private final List<ObservadorMision> observadores = new ArrayList<>();
    private final List<DroneAcuatico> flotaActiva = new ArrayList<>();

    public AsignadorAutomatico(EstrategiaSeleccion estrategia) {
        this.estrategia = estrategia;
    }

    public void setFlota(List<DroneAcuatico> flota) {
        this.flotaActiva.clear();
        this.flotaActiva.addAll(flota);
    }

    public void registrarObservador(ObservadorMision observador) {
        observadores.add(observador);
    }

    public void removerObservador(ObservadorMision observador) {
        observadores.remove(observador);
    }

    public Optional<DroneAcuatico> asignar(Mision mision) {
        if (flotaActiva.isEmpty()) {
            notificarFalloAsignacion(mision);
            return Optional.empty();
        }

        // Simular intento de asignación (incluso buscando alternativo)
        Optional<DroneAcuatico> candidatoOpt = estrategia.seleccionar(flotaActiva, mision);
        
        while (candidatoOpt.isPresent()) {
            DroneAcuatico drone = candidatoOpt.get();
            
            // Validar batería umbral (35%) y que esté disponible
            if (!drone.disponible() || drone.getBateria() < 35) {
                // Si está en FALLO
                if (drone.getEstado() == EstadoDrone.FALLO) {
                    notificarDroneFallo(drone);
                }
                
                // Lo retiramos de los elegibles para buscar el siguiente alternativo
                flotaActiva.remove(drone);
                candidatoOpt = estrategia.seleccionar(flotaActiva, mision);
            } else {
                // Happy path
                System.out.println("Drone " + drone.getId() + " asignado exitosamente.");
                return Optional.of(drone);
            }
        }

        // Si la lista se agotó y no hubo candidato apto
        if (mision.getPrioridad() == Prioridad.CRITICA) {
            notificarFalloAsignacion(mision);
        }
        return Optional.empty();
    }

    private void notificarFalloAsignacion(Mision mision) {
        for (ObservadorMision observer : observadores) {
            observer.notificarFalloAsignacion(mision);
        }
    }

    private void notificarDroneFallo(DroneAcuatico drone) {
        for (ObservadorMision observer : observadores) {
            observer.onDroneFallo(drone);
        }
    }
}
