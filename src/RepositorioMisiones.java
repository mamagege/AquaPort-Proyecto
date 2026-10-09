import java.util.List;

/**
 * Interfaz RepositorioMisiones.
 * 
 * Función: Define el contrato para el almacenamiento y recuperación de misiones.
 * ¿Qué es?: Es una abstracción (interfaz) que representa un almacén de datos genérico.
 * ¿Para qué sirve?: Permite abstraer la forma en la que se guardan los datos (memoria, base de datos, archivo).
 * 
 * Principios SOLID aplicados:
 * - DIP (Inversión de Dependencias): Actúa como una abstracción. Las clases de alto nivel 
 *   (como RegistradorMisiones) dependen de esta interfaz en lugar de depender de una implementación 
 *   concreta (como una BaseDatosMySQL), desacoplando el código.
 * - SRP (Responsabilidad Única): Su única responsabilidad es definir las operaciones de persistencia.
 */
public interface RepositorioMisiones {
    void guardar(Mision mision);
    List<Mision> obtenerTodas();
}
