import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatronesV3Test {

    @Test
    void testCadenaResponsabilidadFallaTemprano() {
        // Arrange
        ValidadorCadena valBateria = spy(new ValidadorBateria());
        ValidadorCadena valCarga = spy(new ValidadorCapacidadCarga());
        ValidadorCadena valZona = spy(new ValidadorZonaActiva());
        
        // Configuramos la cadena: Bateria -> Carga -> Zona
        valBateria.setSiguiente(valCarga).setSiguiente(valZona);
        
        // Un drone con batería muerta (10%)
        DroneAcuatico d = DroneFactory.crearDrone("SUPERFICIAL", "D1", "M", 10, EstadoDrone.DISPONIBLE, "Z1");
        Mision m = new Mision.Builder().id("M1").prioridad(Prioridad.NORMAL).puntoPartida("Z1").puntoLlegada("Z2").tipoCarga(TipoCarga.MUESTRA_AGUA).drone(d).build();
        
        // Act
        boolean resultado = valBateria.validar(m);
        
        // Assert
        assertFalse(resultado);
        verify(valBateria, times(1)).check(m);
        verify(valCarga, never()).validar(any()); // La cadena se detuvo
        verify(valZona, never()).validar(any());
    }

    @Test
    void testCadenaResponsabilidadExito() {
        ValidadorCadena valBateria = spy(new ValidadorBateria());
        ValidadorCadena valCarga = spy(new ValidadorCapacidadCarga());
        
        valBateria.setSiguiente(valCarga);
        
        // Drone con bateria y carga suficiente
        DroneAcuatico d = DroneFactory.crearDrone("SEMISUMERGIDO", "D1", "M", 100, EstadoDrone.DISPONIBLE, "Z1");
        Mision m = new Mision.Builder().id("M1").prioridad(Prioridad.NORMAL).puntoPartida("Z1").puntoLlegada("Z2").tipoCarga(TipoCarga.MUESTRA_AGUA).drone(d).build();
        
        assertTrue(valBateria.validar(m));
        verify(valBateria, times(1)).check(m);
        verify(valCarga, times(1)).validar(m);
    }
    
    @Test
    void testValidadorZonaActiva() {
        ValidadorZonaActiva valZona = new ValidadorZonaActiva();
        
        // Con drone zona correcta
        DroneAcuatico dCorrecto = DroneFactory.crearDrone("SUPERFICIAL", "D1", "M", 100, EstadoDrone.DISPONIBLE, "Z1");
        Mision mCorrecta = new Mision.Builder().id("M2").puntoPartida("Z1").puntoLlegada("Z2").tipoCarga(TipoCarga.MUESTRA_AGUA).drone(dCorrecto).build();
        assertTrue(valZona.check(mCorrecta));
        
        // Con drone zona incorrecta
        DroneAcuatico dIncorrecto = DroneFactory.crearDrone("SUPERFICIAL", "D2", "M", 100, EstadoDrone.DISPONIBLE, "Z2");
        Mision mIncorrecta = new Mision.Builder().id("M3").puntoPartida("Z1").puntoLlegada("Z2").tipoCarga(TipoCarga.MUESTRA_AGUA).drone(dIncorrecto).build();
        assertFalse(valZona.check(mIncorrecta));
    }

    @Test
    void testValidadoresConCarga() {
        ValidadorCapacidadCarga valCarga = new ValidadorCapacidadCarga();
        
        // Carga suficiente vs insuficiente
        DroneAcuatico dCorrecto = DroneFactory.crearDrone("SUPERFICIAL", "D1", "M", 100, EstadoDrone.DISPONIBLE, "Z1");
        Mision mCarga = new Mision.Builder().id("M").puntoPartida("Z1").puntoLlegada("Z2").drone(dCorrecto).tipoCarga(TipoCarga.MUESTRA_AGUA).build();
        assertTrue(valCarga.check(mCarga));
    }
        
    @Test
    void testDecoratorNoAlteraComportamientoBase() {
        DroneAcuatico base = DroneFactory.crearDrone("BUCEADOR", "D1", "M", 100, EstadoDrone.DISPONIBLE, "Z1");
        
        DroneConMonitoreo decorado1 = new DroneConMonitoreo(base);
        DroneConCifrado decorado2 = new DroneConCifrado(decorado1);
        
        // Comportamiento base inalterado
        assertEquals("BUCEADOR", decorado2.getTipo());
        assertEquals("D1", decorado2.getId());
        assertTrue(decorado2.disponible());
        
        // Nueva funcionalidad añadida por los decoradores
        assertFalse(decorado1.isTelemetriaActiva());
        assertFalse(decorado2.isCifradoActivo());
        
        decorado2.registrarTelemetria(); // Pasa a traves de ambos
        
        assertTrue(decorado1.isTelemetriaActiva());
        assertTrue(decorado2.isCifradoActivo());
    }

    @Test
    void testAdaptadorAPIHidricaValoresLimite() {
        APIHidricaExterna mockApi = mock(APIHidricaExterna.class);
        
        // JSON de prueba con decimales y limites
        when(mockApi.obtenerDatosZona("ZONA_NORTE"))
            .thenReturn("{\"waterLevel\": 10.1, \"turbidity\": 49.9}");
            
        AdaptadorAPIHidrica adaptador = new AdaptadorAPIHidrica(mockApi);
        CondicionesHidricas condiciones = adaptador.consultarCondiciones("ZONA_NORTE");
        
        assertEquals(10.1, condiciones.getNivelAgua(), 0.001);
        assertEquals(49.9, condiciones.getTurbidez(), 0.001);
        
        // Prueba integracion con la cadena (ValidadorCondicionesHidricas)
        ValidadorCondicionesHidricas validador = new ValidadorCondicionesHidricas(adaptador);
        DroneAcuatico d = DroneFactory.crearDrone("SUPERFICIAL", "D1", "M", 100, EstadoDrone.DISPONIBLE, "ZONA_NORTE");
        Mision m = new Mision.Builder().id("M1").prioridad(Prioridad.NORMAL).puntoPartida("ZONA_NORTE").puntoLlegada("Z2").tipoCarga(TipoCarga.MUESTRA_AGUA).drone(d).build();
        
        // 10.1 > 10.0 y 49.9 < 50.0 -> Pasa validacion!
        assertTrue(validador.check(m));
        
        // Falla limite
        when(mockApi.obtenerDatosZona("ZONA_NORTE"))
            .thenReturn("{\"waterLevel\": 10.0, \"turbidity\": 50.0}");
        assertFalse(validador.check(m));
    }
    
    @Test
    void testAdaptadorFallaSegura() {
        APIHidricaExterna mockApi = mock(APIHidricaExterna.class);
        when(mockApi.obtenerDatosZona("ZONA_X")).thenReturn("invalid json");
        AdaptadorAPIHidrica adaptador = new AdaptadorAPIHidrica(mockApi);
        CondicionesHidricas cond = adaptador.consultarCondiciones("ZONA_X");
        
        assertEquals(0.0, cond.getNivelAgua());
        assertEquals(0.0, cond.getTurbidez());
    }
}
