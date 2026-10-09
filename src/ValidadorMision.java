/**
 * Clase ValidadorMision.
 * 
 * Función: Contiene las reglas de negocio que determinan si una misión es apta o no.
 * ¿Qué es?: Es un servicio de dominio encargado de aplicar lógica empresarial.
 * ¿Para qué sirve?: Centraliza toda la lógica compleja de validación separándola de las clases de datos.
 * 
 * Principios SOLID aplicados:
 * - SRP (Responsabilidad Única): Esta clase tiene una única razón para cambiar.
 */
public class ValidadorMision {

    public boolean esValida(Mision mision) {
        if (mision == null) return false;
        
        try {
            validarDisponibilidad(mision.getDrone());
            validarPuntoLlegada(mision.getPuntoLlegada());
            return tieneBateriaSuficiente(mision.getDrone());
        } catch (IllegalArgumentException | IllegalStateException e) {
            return false;
        }
    }

    public boolean tieneBateriaSuficiente(DroneAcuatico drone) {
        if (drone == null) return false;
        return drone.bateria() >= 35;
    }

    public void validarPuntoLlegada(String puntoLlegada) {
        if (puntoLlegada == null || puntoLlegada.trim().isEmpty()) {
            throw new IllegalArgumentException("El punto de llegada no puede ser nulo o vacío.");
        }
        if (puntoLlegada.equalsIgnoreCase("Zona Restringida")) {
            throw new IllegalArgumentException("No se pueden realizar misiones hacia la Zona Restringida.");
        }
    }

    public void validarDisponibilidad(DroneAcuatico drone) {
        if (drone == null || !drone.disponible()) {
            throw new IllegalStateException("El drone no está disponible para asignación.");
        }
    }
}
