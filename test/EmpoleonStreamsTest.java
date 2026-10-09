import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

class EmpoleonStreamsTest {

    @Test
    void testEficienciaPorZona() {
        DroneAcuatico d = DroneFactory.crearDrone("SUPERFICIAL", "D", "M", 100, EstadoDrone.DISPONIBLE, "Z1");
        Mision m1 = new Mision.Builder().id("M1").prioridad(Prioridad.NORMAL).puntoPartida("Z1").puntoLlegada("Z2").tipoCarga(TipoCarga.MUESTRA_AGUA).drone(d).estado(EstadoMision.COMPLETADA).build();
        Mision m2 = new Mision.Builder().id("M2").prioridad(Prioridad.NORMAL).puntoPartida("Z1").puntoLlegada("Z2").tipoCarga(TipoCarga.MUESTRA_AGUA).drone(d).estado(EstadoMision.CANCELADA).build();
        Mision m3 = new Mision.Builder().id("M3").prioridad(Prioridad.NORMAL).puntoPartida("Z3").puntoLlegada("Z2").tipoCarga(TipoCarga.MUESTRA_AGUA).drone(d).estado(EstadoMision.COMPLETADA).build();
        
        Map<String, Double> eff = EmpoleonStreams.calcularEficienciaPorZona(List.of(m1, m2, m3));
        assertEquals(0.5, eff.get("Z1"), 0.01);
        assertEquals(1.0, eff.get("Z3"), 0.01);
    }

    @Test
    void testDroneMasExitoso() {
        DroneAcuatico d1 = DroneFactory.crearDrone("SUPERFICIAL", "D1", "M", 100, EstadoDrone.DISPONIBLE, "Z1");
        d1.incrementarEntregas();
        d1.incrementarEntregas();
        
        DroneAcuatico d2 = DroneFactory.crearDrone("SUPERFICIAL", "D2", "M", 100, EstadoDrone.DISPONIBLE, "Z1");
        d2.incrementarEntregas();
        
        Optional<DroneAcuatico> max = EmpoleonStreams.droneMasExitoso(List.of(d1, d2));
        assertTrue(max.isPresent());
        assertEquals("D1", max.get().getId());
    }

    @Test
    void testPlanificarRutaOptima() {
        Waypoint w1 = new Waypoint("W1", "Z1", 0, 0);
        Waypoint w2 = new Waypoint("W2", "Z2", 0, 0);
        
        assertEquals("W1", w1.getId());
        assertEquals(0, w1.getLatitud());
        assertEquals(0, w1.getLongitud());
        
        DroneAcuatico d1 = DroneFactory.crearDrone("SUPERFICIAL", "D1", "M", 50, EstadoDrone.DISPONIBLE, "Z1");
        DroneAcuatico d2 = DroneFactory.crearDrone("SUPERFICIAL", "D2", "M", 90, EstadoDrone.DISPONIBLE, "Z1");
        DroneAcuatico d3 = DroneFactory.crearDrone("SUPERFICIAL", "D3", "M", 100, EstadoDrone.EN_MISION, "Z2");
        DroneAcuatico d4 = DroneFactory.crearDrone("SUPERFICIAL", "D4", "M", 80, EstadoDrone.DISPONIBLE, "Z2");
        
        List<DroneAcuatico> flota = List.of(d1, d2, d3, d4);
        List<Waypoint> ruta = List.of(w1, w2);
        
        List<DroneAcuatico> opt = EmpoleonStreams.planificarRutaOptima(ruta, flota);
        assertEquals(2, opt.size());
        assertEquals("D2", opt.get(0).getId()); // Max bateria en Z1 disponible
        assertEquals("D4", opt.get(1).getId()); // Max bateria en Z2 disponible
        
        Waypoint w3 = new Waypoint("W3", "Z3", 0, 0);
        assertThrows(IllegalStateException.class, () -> {
            EmpoleonStreams.planificarRutaOptima(List.of(w3), flota);
        });
    }

    @Test
    void testBateriaPromedioPorZonaCustom() {
        DroneAcuatico d1 = DroneFactory.crearDrone("SUPERFICIAL", "D1", "M", 50, EstadoDrone.DISPONIBLE, "Z1");
        DroneAcuatico d2 = DroneFactory.crearDrone("SUPERFICIAL", "D2", "M", 90, EstadoDrone.DISPONIBLE, "Z1");
        DroneAcuatico d3 = DroneFactory.crearDrone("SUPERFICIAL", "D3", "M", 20, EstadoDrone.DISPONIBLE, "Z2");
        
        Map<String, Double> promedios = EmpoleonStreams.bateriaPromedioPorZonaCustom(List.of(d1, d2, d3));
        assertEquals(70.0, promedios.get("Z1"), 0.01);
        assertEquals(20.0, promedios.get("Z2"), 0.01);
        
        // Edge case: Empty list
        Map<String, Double> empty = EmpoleonStreams.bateriaPromedioPorZonaCustom(List.of());
        assertTrue(empty.isEmpty());
        
        // Parallel stream to test combiner
        Map<String, Double> parallel = List.of(d1, d2, d3).parallelStream()
            .collect(java.util.stream.Collectors.groupingBy(
                DroneAcuatico::getZona,
                java.util.stream.Collector.of(
                    () -> new double[2],
                    (acc, d) -> { acc[0] += d.getBateria(); acc[1]++; },
                    (acc1, acc2) -> { acc1[0] += acc2[0]; acc1[1] += acc2[1]; return acc1; },
                    acc -> acc[1] == 0 ? 0.0 : acc[0] / acc[1]
                )
            ));
        assertEquals(70.0, parallel.get("Z1"), 0.01);
    }
    
    @Test
    void testMisionWaypoints() {
        DroneAcuatico d = DroneFactory.crearDrone("SUPERFICIAL", "D", "M", 100, EstadoDrone.DISPONIBLE, "Z");
        Waypoint w1 = new Waypoint("W1", "Z1", 0, 0);
        
        Mision m = new Mision.Builder()
            .id("M1")
            .prioridad(Prioridad.NORMAL)
            .puntoPartida("Z1")
            .puntoLlegada("Z2")
            .tipoCarga(TipoCarga.MUESTRA_AGUA)
            .drone(d)
            .estado(EstadoMision.EN_TRANSITO)
            .waypoints(List.of(w1))
            .build();
            
        assertEquals(1, m.getWaypoints().size());
        assertEquals("W1", m.getWaypoints().get(0).getId());
    }
}
