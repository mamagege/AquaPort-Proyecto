import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ValidadorMisionTest {
    private ValidadorMision v;

    @BeforeEach
    void setUp() {
        v = new ValidadorMision();
    }

    @Test
    @DisplayName("Drone con batería >= 35% puede ser asignado")
    void droneBateriaSuficiente_puedeAsignarse() {
        DroneAcuatico d = DroneFactory.crearDrone("SUPERFICIAL", "AR-01", "Aqua-Ranger 100", 85, EstadoDrone.DISPONIBLE, "Embalse Norte");
        assertTrue(v.tieneBateriaSuficiente(d));
    }

    @Test
    @DisplayName("Drone con batería < 35% NO puede ser asignado")
    void droneBateriaCritica_noAsignable() {
        DroneAcuatico d = DroneFactory.crearDrone("SUPERFICIAL", "AR-03", "Aqua-Ranger 100", 18, EstadoDrone.EN_MISION, "Laguna Sur");
        assertFalse(v.tieneBateriaSuficiente(d));
    }

    @Test
    @DisplayName("Punto de llegada nulo lanza IllegalArgumentException")
    void puntoLlegadaNulo_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> v.validarPuntoLlegada(null));
    }

    @Test
    @DisplayName("Punto de llegada vacío lanza IllegalArgumentException")
    void puntoLlegadaVacio_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> v.validarPuntoLlegada("   "));
    }

    @Test
    @DisplayName("Drone no disponible (disponible=false) lanza IllegalStateException")
    void droneNoDisponible_lanzaExcepcion() {
        DroneAcuatico d = DroneFactory.crearDrone("SUPERFICIAL", "AR-04", "Aqua-Ranger 100", 100, EstadoDrone.MANTENIMIENTO, "Embalse Sur");
        assertThrows(IllegalStateException.class, () -> v.validarDisponibilidad(d));
    }

    @Test
    @DisplayName("Zona de destino restringida lanza IllegalArgumentException")
    void zonaDestinoInvalida_lanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> v.validarPuntoLlegada("Zona Restringida"));
    }

    @Test
    @DisplayName("Mision válida retorna true")
    void misionValida_retornaTrue() {
        DroneAcuatico d = DroneFactory.crearDrone("SUPERFICIAL", "AR-10", "Aqua-Ranger", 100, EstadoDrone.DISPONIBLE, "Centro");
        Mision m = new Mision.Builder()
            .id("M-01")
            .drone(d)
            .puntoPartida("Centro")
            .puntoLlegada("Norte")
            .tipoCarga(TipoCarga.MUESTRA_AGUA)
            .prioridad(Prioridad.NORMAL)
            .build();
        assertTrue(v.esValida(m));
    }

    @Test
    @DisplayName("Mision nula retorna false")
    void misionNula_retornaFalse() {
        assertFalse(v.esValida(null));
    }

    @Test
    @DisplayName("Mision invalida por excepcion retorna false")
    void misionInvalida_retornaFalse() {
        DroneAcuatico dValido = DroneFactory.crearDrone("SUPERFICIAL", "AR-12", "Aqua-Ranger", 100, EstadoDrone.DISPONIBLE, "Centro");
        Mision m = new Mision.Builder()
            .id("M-02")
            .drone(dValido)
            .puntoPartida("Centro")
            .puntoLlegada("Zona Restringida") // ValidadorMision arrojará excepcion aquí.
            .tipoCarga(TipoCarga.MUESTRA_AGUA)
            .prioridad(Prioridad.NORMAL)
            .build();
            
        assertFalse(v.esValida(m));
    }

    @Test
    @DisplayName("tieneBateriaSuficiente con drone nulo retorna false")
    void droneNulo_bateriaFalsa() {
        assertFalse(v.tieneBateriaSuficiente(null));
    }

    @Test
    @DisplayName("validarDisponibilidad con drone nulo lanza IllegalStateException")
    void droneNulo_disponibilidadLanzaExcepcion() {
        assertThrows(IllegalStateException.class, () -> v.validarDisponibilidad(null));
    }
}
