import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlanificadorIntegracionTest {

    @Test
    @DisplayName("IT: Flujo completo Enterprise")
    void testFlujoCompleto() throws Exception {
        // Configuramos dependencias reales/fakes
        AsignadorAutomatico asignador = new AsignadorAutomatico(new MayorBateriaStrategy()) {
            @Override
            public boolean isZonaActiva(String zona) {
                return true;
            }
            @Override
            public Mision asignarMultiEtapa(String origen, String destino, TipoCarga carga) {
                DroneAcuatico d = DroneFactory.crearDrone("BUCEADOR", "D-INT", "G", 100, EstadoDrone.DISPONIBLE, origen);
                return new Mision.Builder().id("M-INT").puntoPartida(origen).puntoLlegada(destino).tipoCarga(carga).drone(d).build();
            }
        };

        ProveedorCondiciones clima = new ProveedorCondiciones() {
            @Override
            public CondicionesHidricas consultarCondiciones(String zona) {
                return new CondicionesHidricas(20.0, 10.0);
            }
        };

        PlanificadorRuta planificador = new PlanificadorRuta(asignador, clima);

        List<Waypoint> ruta = Arrays.asList(
            new Waypoint("W1", "Embalse Norte", 0, 0),
            new Waypoint("W2", "Canal Central", 1, 1),
            new Waypoint("W3", "Laboratorio Sur", 2, 2)
        );

        // Flujo: solicitud -> planificacion -> asignacion -> ejecucion simulada
        boolean exito = planificador.ejecutarRuta(ruta, TipoCarga.MUESTRA_AGUA);
        assertTrue(exito, "El flujo completo de integración debe ser exitoso");
    }
}
