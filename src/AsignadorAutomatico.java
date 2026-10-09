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
                // SonarQube fix: Use logger instead of System.out.println
                java.util.logging.Logger.getLogger(AsignadorAutomatico.class.getName())
                    .info("Drone " + drone.getId() + " asignado exitosamente.");
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

    public Mision asignarMultiEtapa(String origen, String destino, TipoCarga carga) throws FalloDroneException, BateriaCriticaException {
        // En un caso real, busca un drone y lo asigna.
        return null;
    }

    public boolean isZonaActiva(String zona) {
        return true;
    }

    public void reasignarDrone(String zona) {
    }

    public void validarCustodia(Mision mision) throws CustodiaInterrumpidaException {
    }

    public void retornarABase(String zona) {
    }
}
