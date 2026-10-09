/**
 * Clase Mision que implementa el patrón Builder.
 * 
 * Guía paso a paso para replicar el patrón Builder:
 * 
 * 1. Atributos inmutables en la clase principal: Se declaran como private final.
 * 2. Constructor privado: Recibe el Builder como parámetro y asigna sus valores a la clase.
 * 3. Clase interna estática (Builder): Se declara public static class Builder.
 * 4. Atributos mutables en el Builder: Se declaran los mismos atributos, pero sin final.
 * 5. Métodos de configuración (Setters encadenables): Retornan la misma instancia (this).
 * 6. Método build() con validaciones: Verifica las reglas de negocio y retorna la nueva instancia.
 */
public class Mision {
    private final String id;
    private final DroneAcuatico drone;
    private final String puntoPartida;
    private final String puntoLlegada;
    private final TipoCarga tipoCarga;
    private final EstadoMision estado;

    private Mision(Builder builder) {
        this.id = builder.id;
        this.drone = builder.drone;
        this.puntoPartida = builder.puntoPartida;
        this.puntoLlegada = builder.puntoLlegada;
        this.tipoCarga = builder.tipoCarga;
        this.estado = builder.estado;
    }

    public String getId() { return id; }
    public DroneAcuatico getDrone() { return drone; }
    public String getPuntoPartida() { return puntoPartida; }
    public String getPuntoLlegada() { return puntoLlegada; }
    public TipoCarga getTipoCarga() { return tipoCarga; }
    public EstadoMision getEstado() { return estado; }

    public static class Builder {
        private String id;
        private DroneAcuatico drone;
        private String puntoPartida;
        private String puntoLlegada;
        private TipoCarga tipoCarga;
        private EstadoMision estado = EstadoMision.PENDIENTE; // Valor por defecto

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder drone(DroneAcuatico drone) {
            this.drone = drone;
            return this;
        }

        public Builder puntoPartida(String puntoPartida) {
            this.puntoPartida = puntoPartida;
            return this;
        }

        public Builder puntoLlegada(String puntoLlegada) {
            this.puntoLlegada = puntoLlegada;
            return this;
        }

        public Builder tipoCarga(TipoCarga tipoCarga) {
            this.tipoCarga = tipoCarga;
            return this;
        }

        public Builder estado(EstadoMision estado) {
            this.estado = estado;
            return this;
        }

        public Mision build() {
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalStateException("El id de la misión no puede ser nulo o vacío.");
            }
            if (drone == null) {
                throw new IllegalStateException("El drone asignado no puede ser nulo.");
            }
            if (!drone.disponible()) {
                throw new IllegalStateException("El drone asignado no está disponible en este momento.");
            }
            if (puntoPartida == null || puntoPartida.trim().isEmpty()) {
                throw new IllegalStateException("El punto de partida no puede ser nulo o vacío.");
            }
            if (puntoLlegada == null || puntoLlegada.trim().isEmpty()) {
                throw new IllegalStateException("El punto de llegada no puede ser nulo o vacío.");
            }
            if (tipoCarga == null) {
                throw new IllegalStateException("El tipo de carga no puede ser nulo.");
            }

            return new Mision(this);
        }
    }
}
