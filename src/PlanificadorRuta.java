import java.util.List;

public class PlanificadorRuta {

    private final AsignadorAutomatico asignador;
    private final ProveedorCondiciones clima;

    public PlanificadorRuta(AsignadorAutomatico asignador, ProveedorCondiciones clima) {
        this.asignador = asignador;
        this.clima = clima;
    }

    public boolean ejecutarRuta(List<Waypoint> ruta, TipoCarga tipoCarga) throws Exception {
        if (ruta == null || ruta.size() < 2) return false;

        for (int i = 0; i < ruta.size() - 1; i++) {
            Waypoint origen = ruta.get(i);
            Waypoint destino = ruta.get(i + 1);

            if (!asignador.isZonaActiva(origen.getZona()) || !asignador.isZonaActiva(destino.getZona())) {
                throw new ZonaInactivaException("Zona inactiva en el tramo " + origen.getZona() + " - " + destino.getZona());
            }

            CondicionesHidricas condOrigen = clima.consultarCondiciones(origen.getZona());
            CondicionesHidricas condDestino = clima.consultarCondiciones(destino.getZona());

            if (condOrigen.getTurbidez() > 50 || condDestino.getTurbidez() > 50) {
                throw new CondicionAdversaException("Condición hídrica adversa");
            }

            try {
                Mision mision = asignador.asignarMultiEtapa(origen.getZona(), destino.getZona(), tipoCarga);
                asignador.validarCustodia(mision);
            } catch (FalloDroneException e) {
                asignador.reasignarDrone(origen.getZona());
                throw e; // Propagamos para abortar o que el controlador maneje
            } catch (BateriaCriticaException e) {
                asignador.retornarABase(origen.getZona());
                throw e;
            }
        }
        return true;
    }
}
