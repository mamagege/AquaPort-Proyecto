import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsignadorAutomaticoTest {

    @Mock
    EstrategiaSeleccion estrategiaMock;
    
    @Mock
    ObservadorMision observadorMock;
    
    @InjectMocks
    AsignadorAutomatico asignador;

    @Test
    @DisplayName("Asignación exitosa (happy path)")
    void asignacionExitosa() {
        Mision mision = mock(Mision.class);
        DroneAcuatico droneOptimo = DroneFactory.crearDrone("SUPERFICIAL", "A-01", "Model", 100, EstadoDrone.DISPONIBLE, "Z1");
        
        asignador.setFlota(List.of(droneOptimo));
        when(estrategiaMock.seleccionar(any(), eq(mision))).thenReturn(Optional.of(droneOptimo));
        
        Optional<DroneAcuatico> resultado = asignador.asignar(mision);
        
        assertTrue(resultado.isPresent());
        assertEquals(droneOptimo, resultado.get());
        verify(observadorMock, never()).notificarFalloAsignacion(any());
    }

    @Test
    @DisplayName("Misión CRÍTICA sin drones disponibles notifica a todos los observadores")
    void misionCritica_sinDrones_notificaObservadores() {
        Mision mision = mock(Mision.class);
        when(mision.getPrioridad()).thenReturn(Prioridad.CRITICA);
        DroneAcuatico droneFalso = DroneFactory.crearDrone("SUPERFICIAL", "A-01", "Model", 10, EstadoDrone.EN_MISION, "Z1");
        
        asignador.setFlota(List.of(droneFalso));
        when(estrategiaMock.seleccionar(any(), eq(mision))).thenReturn(Optional.empty());
        asignador.registrarObservador(observadorMock);
        
        Optional<DroneAcuatico> resultado = asignador.asignar(mision);
        
        assertFalse(resultado.isPresent());
        verify(observadorMock, times(1)).notificarFalloAsignacion(mision);
    }

    @Test
    @DisplayName("Drone encontrado en FALLO durante asignación (notifica + busca alternativo)")
    void droneEnFalloDuranteAsignacion_notificaYBuscaAlternativo() {
        Mision mision = mock(Mision.class);
        DroneAcuatico droneFallo = DroneFactory.crearDrone("SUPERFICIAL", "A-FALLO", "Model", 80, EstadoDrone.FALLO, "Z1");
        DroneAcuatico droneBueno = DroneFactory.crearDrone("SUPERFICIAL", "A-BUENO", "Model", 90, EstadoDrone.DISPONIBLE, "Z1");
        
        asignador.setFlota(List.of(droneFallo, droneBueno));
        
        // La primera vez devuelve el dron con FALLO. La segunda devuelve el droneBueno.
        when(estrategiaMock.seleccionar(anyList(), eq(mision)))
            .thenReturn(Optional.of(droneFallo))
            .thenReturn(Optional.of(droneBueno));
            
        asignador.registrarObservador(observadorMock);
        
        Optional<DroneAcuatico> resultado = asignador.asignar(mision);
        
        assertTrue(resultado.isPresent());
        assertEquals(droneBueno, resultado.get());
        verify(observadorMock, times(1)).onDroneFallo(droneFallo);
    }

    @Test
    @DisplayName("Lista de drones vacía")
    void listaDronesVacia() {
        Mision mision = mock(Mision.class);
        asignador.setFlota(List.of());
        asignador.registrarObservador(observadorMock);
        
        Optional<DroneAcuatico> resultado = asignador.asignar(mision);
        
        assertFalse(resultado.isPresent());
        verify(observadorMock, times(1)).notificarFalloAsignacion(mision);
    }

    @Test
    @DisplayName("Batería exactamente en el umbral mínimo (caso edge 35%)")
    void bateriaExactamenteUmbral_CasoEdge() {
        Mision mision = mock(Mision.class);
        DroneAcuatico droneUmbral = DroneFactory.crearDrone("SUPERFICIAL", "A-01", "Model", 35, EstadoDrone.DISPONIBLE, "Z1");
        
        asignador.setFlota(List.of(droneUmbral));
        when(estrategiaMock.seleccionar(any(), eq(mision))).thenReturn(Optional.of(droneUmbral));
        
        Optional<DroneAcuatico> resultado = asignador.asignar(mision);
        
        assertTrue(resultado.isPresent());
        assertEquals(droneUmbral, resultado.get());
        verify(observadorMock, never()).notificarFalloAsignacion(any());
    }
}
