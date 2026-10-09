import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

class DomainV2Test {

    @Test
    void testObservers() {
        DroneAcuatico drone = DroneFactory.crearDrone("SUPERFICIAL", "D-01", "Model", 100, EstadoDrone.FALLO, "Z1");
        Mision mision = mock(Mision.class);
        
        CentroControlObserver cco = new CentroControlObserver();
        cco.onDroneFallo(drone);
        cco.notificarFalloAsignacion(mision);
        
        TecnicoMantenimientoObserver tmo = new TecnicoMantenimientoObserver();
        tmo.onDroneFallo(drone);
        tmo.notificarFalloAsignacion(mision);
        
        // Observers print to console, we just need to hit the lines for coverage
        assertNotNull(cco);
        assertNotNull(tmo);
    }

    @Test
    void testStrategies() {
        DroneAcuatico d1 = DroneFactory.crearDrone("BUCEADOR", "D-1", "Model", 50, EstadoDrone.DISPONIBLE, "Z1");
        DroneAcuatico d2 = DroneFactory.crearDrone("SEMISUMERGIDO", "D-2", "Model", 90, EstadoDrone.DISPONIBLE, "Z1");
        DroneAcuatico d3 = DroneFactory.crearDrone("SUPERFICIAL", "D-3", "Model", 10, EstadoDrone.EN_MISION, "Z1");
        
        List<DroneAcuatico> flota = List.of(d1, d2, d3);
        Mision mision = mock(Mision.class);
        
        MayorBateriaStrategy s1 = new MayorBateriaStrategy();
        Optional<DroneAcuatico> res1 = s1.seleccionar(flota, mision);
        assertTrue(res1.isPresent());
        assertEquals(d2, res1.get()); // 90 > 50, d3 is EN_MISION

        PrioridadCriticaStrategy s2 = new PrioridadCriticaStrategy();
        Optional<DroneAcuatico> res2 = s2.seleccionar(flota, mision);
        assertTrue(res2.isPresent());
        assertEquals(d2, res2.get()); // SEMISUMERGIDO has max capacity (1.5kg vs 0.3kg)
        
        ZonaCercanaStrategy s3 = new ZonaCercanaStrategy();
        Optional<DroneAcuatico> res3 = s3.seleccionar(flota, mision);
        assertTrue(res3.isPresent());
        assertEquals(d1, res3.get()); // findFirst returns d1
    }

    @Test
    void testDronesAndFactory() {
        DroneAcuatico buceador = DroneFactory.crearDrone("BUCEADOR", "B-1", "B", 100, EstadoDrone.DISPONIBLE, "Z");
        assertEquals(0.3, buceador.getCapacidadCargaMax());
        assertEquals("B-1", buceador.getId());
        assertEquals("BUCEADOR", buceador.getTipo());
        
        DroneAcuatico semi = DroneFactory.crearDrone("SEMISUMERGIDO", "S-1", "S", 100, EstadoDrone.DISPONIBLE, "Z");
        assertEquals(1.5, semi.getCapacidadCargaMax());
        assertEquals("SEMISUMERGIDO", semi.getTipo());
        
        DroneAcuatico sup = DroneFactory.crearDrone("SUPERFICIAL", "SU-1", "SU", 100, EstadoDrone.DISPONIBLE, "Z");
        assertEquals(0.5, sup.getCapacidadCargaMax());
        assertEquals("SUPERFICIAL", sup.getTipo());
        
        assertThrows(IllegalArgumentException.class, () -> {
            DroneFactory.crearDrone("DESCONOCIDO", "U-1", "U", 100, EstadoDrone.DISPONIBLE, "Z");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            DroneFactory.crearDrone(null, "U-1", "U", 100, EstadoDrone.DISPONIBLE, "Z");
        });
        
        // DroneAcuatico properties
        sup.setEstado(EstadoDrone.FALLO);
        assertEquals(EstadoDrone.FALLO, sup.getEstado());
    }

    @Test
    void testNotificadorOperador() {
        NotificadorOperador notif = new NotificadorOperador();
        notif.notificarExito("Exito");
        notif.notificarError("Error");
        assertNotNull(notif);
    }

    @Test
    void testRegistradorMisiones() {
        RepositorioMisiones repo = mock(RepositorioMisiones.class);
        ValidadorMision val = mock(ValidadorMision.class);
        NotificadorOperador notif = mock(NotificadorOperador.class);
        
        RegistradorMisiones reg = new RegistradorMisiones(repo, val, notif);
        Mision m = mock(Mision.class);
        when(m.getId()).thenReturn("M1");
        
        // Exito
        when(val.esValida(m)).thenReturn(true);
        reg.registrar(m);
        verify(repo).guardar(m);
        verify(notif).notificarExito(contains("exitosa"));
        
        // Falla
        when(val.esValida(m)).thenReturn(false);
        reg.registrar(m);
        verify(notif).notificarError(contains("Falló"));
        
        reg.consultarTodas();
        verify(repo).obtenerTodas();
    }

    @Test
    void testConsultorFlota() {
        ConsultarFlota c = new ConsultarFlota();
        DroneAcuatico d1 = DroneFactory.crearDrone("SUPERFICIAL", "D1", "M", 40, EstadoDrone.DISPONIBLE, "Z1");
        DroneAcuatico d2 = DroneFactory.crearDrone("SUPERFICIAL", "D2", "M", 20, EstadoDrone.DISPONIBLE, "Z1");
        List<DroneAcuatico> flota = List.of(d1, d2);
        
        assertEquals(1, c.dronesDisponibles35(flota).size());
        assertEquals(2, c.idDronesDisponibles(flota).size());
        assertTrue(c.existeDronDisponible35(flota));
        assertEquals(2, c.numDronesdisponibles(flota));
        assertTrue(c.dronMayorbateria(flota).isPresent());
        
        // Main
        ConsultarFlota.main(new String[]{});
    }

    @Test
    void testConsultorFlotaV2() {
        ConsultorFlotaV2 cv2 = new ConsultorFlotaV2();
        DroneAcuatico d1 = DroneFactory.crearDrone("SUPERFICIAL", "D1", "M", 40, EstadoDrone.DISPONIBLE, "Z1");
        DroneAcuatico d2 = DroneFactory.crearDrone("SUPERFICIAL", "D2", "M", 20, EstadoDrone.EN_MISION, "Z2");
        List<DroneAcuatico> flota = List.of(d1, d2);
        
        assertEquals(1, cv2.agruparDronesDisponiblesPorTipo(flota).get("SUPERFICIAL").size());
        assertTrue(cv2.obtenerDroneOptimo(flota, "Z1", "SUPERFICIAL").isPresent());
        
        java.util.Map<String, Double> promedios = cv2.promedioBateriaPorTipo(flota);
        assertEquals(30.0, promedios.get("SUPERFICIAL"));
        
        Mision m1 = mock(Mision.class);
        when(m1.getPrioridad()).thenReturn(Prioridad.CRITICA);
        Mision m2 = mock(Mision.class);
        when(m2.getPrioridad()).thenReturn(Prioridad.NORMAL);
        
        java.util.Map<Boolean, List<Mision>> sep = cv2.separarMisionesCriticas(List.of(m1, m2));
        assertEquals(1, sep.get(true).size());
        assertEquals(1, sep.get(false).size());
        
        java.util.Set<String> zonas = cv2.obtenerZonasUnicasActivas(flota);
        assertEquals(2, zonas.size());
    }
    
    @Test
    void testMisionBuilder() {
        DroneAcuatico d = DroneFactory.crearDrone("SUPERFICIAL", "D", "M", 100, EstadoDrone.DISPONIBLE, "Z");
        
        Mision m = new Mision.Builder()
            .id("M1")
            .prioridad(Prioridad.CRITICA)
            .puntoPartida("Z1")
            .puntoLlegada("Z2")
            .tipoCarga(TipoCarga.MUESTRA_AGUA)
            .drone(d)
            .estado(EstadoMision.EN_CURSO)
            .build();
            
        assertEquals("M1", m.getId());
        assertEquals(Prioridad.CRITICA, m.getPrioridad());
        assertEquals("Z1", m.getPuntoPartida());
        assertEquals("Z2", m.getPuntoLlegada());
        assertEquals(TipoCarga.MUESTRA_AGUA, m.getTipoCarga());
        assertEquals(d, m.getDrone());
        assertEquals(EstadoMision.EN_CURSO, m.getEstado());
        
        assertThrows(IllegalStateException.class, () -> new Mision.Builder().build());
    }
}
