public class Mision {
    private final String id;
    private final DroneAcuatico drone;
    private final String puntoPartida;
    private final String puntoLlegada;
    private final TipoCarga tipoCarga;
    private EstadoMision estado;
    private final Prioridad prioridad;
    private final java.util.List<Waypoint> waypoints; // Para la v3

    private Mision(Builder builder) {
        this.id = builder.id;
        this.drone = builder.drone;
        this.puntoPartida = builder.puntoPartida;
        this.puntoLlegada = builder.puntoLlegada;
        this.tipoCarga = builder.tipoCarga;
        this.estado = builder.estado;
        this.prioridad = builder.prioridad;
        this.waypoints = builder.waypoints != null ? builder.waypoints : new java.util.ArrayList<>();
    }

    public String getId() { return id; }
    public DroneAcuatico getDrone() { return drone; }
    public String getPuntoPartida() { return puntoPartida; }
    public String getPuntoLlegada() { return puntoLlegada; }
    public TipoCarga getTipoCarga() { return tipoCarga; }
    public EstadoMision getEstado() { return estado; }
    public Prioridad getPrioridad() { return prioridad; }
    public java.util.List<Waypoint> getWaypoints() { return waypoints; }
    
    public void setEstado(EstadoMision estado) { this.estado = estado; }

    public static class Builder {
        private String id;
        private DroneAcuatico drone;
        private String puntoPartida;
        private String puntoLlegada;
        private TipoCarga tipoCarga;
        private EstadoMision estado = EstadoMision.PENDIENTE;
        private Prioridad prioridad = Prioridad.NORMAL;
        private java.util.List<Waypoint> waypoints = new java.util.ArrayList<>();

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
        
        public Builder waypoints(java.util.List<Waypoint> waypoints) {
            this.waypoints = waypoints;
            return this;
        }

        public Builder estado(EstadoMision estado) {
            this.estado = estado;
            return this;
        }

        public Builder prioridad(Prioridad prioridad) {
            this.prioridad = prioridad;
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
            if (prioridad == null) {
                throw new IllegalStateException("La prioridad no puede ser nula.");
            }

            return new Mision(this);
        }
    }
}
