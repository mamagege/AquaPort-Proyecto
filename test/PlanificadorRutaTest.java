import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlanificadorRutaTest {

    @Mock
    private AsignadorAutomatico asignadorMock;
    @Mock
    private ProveedorCondiciones climaMock;

    @InjectMocks
    private PlanificadorRuta planificador;

    private List<Waypoint> rutaBase;

    @BeforeEach
    void setUp() {
        rutaBase = Arrays.asList(
            new Waypoint("W1", "Z1", 0, 0),
            new Waypoint("W2", "Z2", 1, 1),
            new Waypoint("W3", "Z3", 2, 2)
        );
    }

    @Test
    @DisplayName("Unit: Planificación de ruta exitosa (Happy Path)")
    void testPlanificacionExitosa() throws Exception {
        DroneAcuatico d = DroneFactory.crearDrone("SUPERFICIAL", "D1", "M", 100, EstadoDrone.DISPONIBLE, "Z1");
        Mision m = new Mision.Builder().id("M1").puntoPartida("Z1").puntoLlegada("Z2").tipoCarga(TipoCarga.MUESTRA_AGUA).drone(d).build();
        
        when(asignadorMock.isZonaActiva(anyString())).thenReturn(true);
        when(asignadorMock.asignarMultiEtapa(any(), any(), any())).thenReturn(m);
        when(climaMock.consultarCondiciones(anyString())).thenReturn(new CondicionesHidricas(20.0, 10.0));
        
        boolean exito = planificador.ejecutarRuta(rutaBase, TipoCarga.MUESTRA_AGUA);
        assertTrue(exito);
        verify(asignadorMock, times(2)).asignarMultiEtapa(any(), any(), any());
    }

    @Test
    @DisplayName("Unit: Fallo de drone en waypoint - reasignación")
    void testFalloEnWaypoint() throws Exception {
        when(asignadorMock.isZonaActiva(anyString())).thenReturn(true);
        when(climaMock.consultarCondiciones(anyString())).thenReturn(new CondicionesHidricas(20.0, 10.0));
        // Simulamos que al pedir el segundo tramo, falla
        when(asignadorMock.asignarMultiEtapa(any(), any(), any()))
            .thenReturn(mock(Mision.class))
            .thenThrow(new FalloDroneException("Fallo motor en Z2"));
        
        assertThrows(FalloDroneException.class, () -> {
            planificador.ejecutarRuta(rutaBase, TipoCarga.MUESTRA_AGUA);
        });
        
        verify(asignadorMock, atLeastOnce()).reasignarDrone(anyString());
    }

    @Test
    @DisplayName("Unit: Zona destino inactiva")
    void testZonaInactiva() throws Exception {
        when(asignadorMock.isZonaActiva("Z3")).thenReturn(false);
        when(asignadorMock.isZonaActiva("Z1")).thenReturn(true);
        when(asignadorMock.isZonaActiva("Z2")).thenReturn(true);
        when(climaMock.consultarCondiciones(anyString())).thenReturn(new CondicionesHidricas(20.0, 10.0));
        when(asignadorMock.asignarMultiEtapa(any(), any(), any())).thenReturn(mock(Mision.class));
        
        assertThrows(ZonaInactivaException.class, () -> {
            planificador.ejecutarRuta(rutaBase, TipoCarga.MUESTRA_AGUA);
        });
    }

    @Test
    @DisplayName("Unit: Condición hídrica adversa")
    void testCondicionAdversa() throws Exception {
        when(asignadorMock.isZonaActiva(anyString())).thenReturn(true);
        // Z2 tiene mal clima
        when(climaMock.consultarCondiciones("Z2")).thenReturn(new CondicionesHidricas(20.0, 90.0)); // turbidez alta
        when(climaMock.consultarCondiciones("Z1")).thenReturn(new CondicionesHidricas(20.0, 10.0));
        
        assertThrows(CondicionAdversaException.class, () -> {
            planificador.ejecutarRuta(rutaBase, TipoCarga.MUESTRA_AGUA);
        });
    }

    @Test
    @DisplayName("Unit: Cadena de custodia interrumpida")
    void testCustodiaInterrumpida() throws Exception {
        when(asignadorMock.isZonaActiva(anyString())).thenReturn(true);
        when(climaMock.consultarCondiciones(anyString())).thenReturn(new CondicionesHidricas(20.0, 10.0));
        
        when(asignadorMock.asignarMultiEtapa(any(), any(), any())).thenReturn(mock(Mision.class));
        doThrow(new CustodiaInterrumpidaException("Firma digital inválida en Z2")).when(asignadorMock).validarCustodia(any());
        
        assertThrows(CustodiaInterrumpidaException.class, () -> {
            planificador.ejecutarRuta(rutaBase, TipoCarga.MUESTRA_AGUA);
        });
    }

    @Test
    @DisplayName("Unit: Batería crítica en mitad de ruta")
    void testBateriaCritica() throws Exception {
        when(asignadorMock.isZonaActiva(anyString())).thenReturn(true);
        when(climaMock.consultarCondiciones(anyString())).thenReturn(new CondicionesHidricas(20.0, 10.0));
        
        when(asignadorMock.asignarMultiEtapa(any(), any(), any())).thenThrow(new BateriaCriticaException("Batería al 5%"));
        
        assertThrows(BateriaCriticaException.class, () -> {
            planificador.ejecutarRuta(rutaBase, TipoCarga.MUESTRA_AGUA);
        });
        // verify retorno a base
        verify(asignadorMock).retornarABase(any());
    }
}
