import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Clase NotificadorOperador.
 * 
 * Función: Enviar mensajes, alertas y reportes al operador del sistema.
 * ¿Qué es?: Un componente de salida de información (Output Port).
 * ¿Para qué sirve?: Muestra información relevante, éxitos o errores sobre el estado 
 * de las operaciones al usuario final u operador del programa.
 * 
 * Principios SOLID aplicados:
 * - SRP (Responsabilidad Única): Su única razón para cambiar es si se modifica 
 *   el medio o el formato en el que se notifican los mensajes. Si el día de mañana 
 *   se pasa de imprimir en consola a enviar un Email o un SMS, solo se modificará esta clase.
 */
public class NotificadorOperador {
    private static final Logger LOGGER = Logger.getLogger(NotificadorOperador.class.getName());

    public void notificarExito(String mensaje) {
        if (LOGGER.isLoggable(Level.INFO)) {
            LOGGER.info(String.format("✅ [OPERADOR] %s", mensaje));
        }
    }
    
    public void notificarError(String mensaje) {
        if (LOGGER.isLoggable(Level.SEVERE)) {
            LOGGER.severe(String.format("🛑 [OPERADOR ERROR] %s", mensaje));
        }
    }
}
