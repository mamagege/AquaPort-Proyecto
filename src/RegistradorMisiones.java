import java.util.List;

/**
 * Clase RegistradorMisiones.
 * 
 * Función: Orquesta y coordina el flujo principal (caso de uso) de registrar misiones.
 * ¿Qué es?: Un orquestador / manejador de casos de uso (Use Case Handler).
 * ¿Para qué sirve?: Recibe una misión, solicita su validación, ordena su persistencia 
 * y finalmente emite una notificación con el resultado del proceso.
 * 
 * Principios SOLID aplicados:
 * - SRP (Responsabilidad Única): Su única razón para cambiar es si se altera el flujo 
 *   del proceso de registro. Delega las subtareas a otras clases (no valida, no guarda 
 *   por su cuenta, no formatea mensajes).
 * - DIP (Inversión de Dependencias): Sus dependencias (RepositorioMisiones, ValidadorMision, 
 *   NotificadorOperador) le son inyectadas a través del constructor. Al no crear instancias 
 *   concretas internamente (no usa 'new BDConcreta()'), la clase es altamente testeable y 
 *   no está rígidamente acoplada a implementaciones específicas.
 */
public class RegistradorMisiones {
    // Inyección de dependencias (DIP)
    private final RepositorioMisiones repositorio;
    private final ValidadorMision validador;
    private final NotificadorOperador notificador;

    public RegistradorMisiones(RepositorioMisiones repositorio, ValidadorMision validador, NotificadorOperador notificador) {
        this.repositorio = repositorio;
        this.validador = validador;
        this.notificador = notificador;
    }

    public void registrar(Mision mision) {
        if (validador.esValida(mision)) {
            repositorio.guardar(mision);
            notificador.notificarExito("Misión '" + mision.getId() + "' registrada exitosamente.");
        } else {
            notificador.notificarError("No se pudo registrar la misión '" + mision.getId() + "'. Falló la validación de negocio.");
        }
    }

    public List<Mision> consultarTodas() {
        return repositorio.obtenerTodas();
    }
}
