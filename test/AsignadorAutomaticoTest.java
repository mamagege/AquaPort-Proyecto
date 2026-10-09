import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AsignadorAutomaticoTest {

    private AsignadorAutomatico asignador;
    private DroneObserver mockCentroControl;
    private DroneObserver mockTecnico;

    @BeforeEach
    void setUp() {
        // Inicializamos el asignador con una estrategia cualquiera
        asignador = new AsignadorAutomatico(new MayorBateriaStrategy());

        // Creamos los mocks de los observadores
        mockCentroControl = mock(DroneObserver.class);
        mockTecnico = mock(DroneObserver.class);

        // Registramos los observadores en el asignador
        asignador.addObserver(mockCentroControl);
        asignador.addObserver(mockTecnico);
    }

    @Test
    @DisplayName("Al registrar misión con drone en FALLO, los observadores son notificados exactamente una vez")
    void alRegistrarMisionConDroneEnFallo_NotificaObservadores() {
        // ARRANGE
        // Creamos un drone en estado de FALLO usando el Factory Method
        DroneAcuatico droneFallo = DroneFactory.crearDrone("SUPERFICIAL", "AR-FALLO", "Aqua-Ranger", 10, EstadoDrone.FALLO, "Centro");
        
        // Creamos una misión utilizando reflexión o forzando el estado ya que Mision.Builder requiere disponibilidad.
        // Espera, Mision.Builder lanza excepcion si el drone no esta disponible. 
        // El drone en FALLO devuelve false en disponible().
        // Para probar el AsignadorAutomatico independiente de Mision.Builder, podemos mockear Mision.
        Mision mockMision = mock(Mision.class);
        when(mockMision.getDrone()).thenReturn(droneFallo);

        // ACT & ASSERT
        // Al registrar la misión, debe lanzar IllegalStateException
        assertThrows(IllegalStateException.class, () -> asignador.registrarMision(mockMision));

        // VERIFY: Comprobamos que el método onDroneFallo se invocó exactamente 1 vez en ambos mocks
        verify(mockCentroControl, times(1)).onDroneFallo(droneFallo);
        verify(mockTecnico, times(1)).onDroneFallo(droneFallo);
    }
}
