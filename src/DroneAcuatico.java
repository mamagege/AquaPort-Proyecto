public abstract class DroneAcuatico {
    private final String id;
    private final String modelo;
    private int bateria;
    private EstadoDrone estado;
    private final String zona;
    private final double capacidadCargaMax;

    public DroneAcuatico(String id, String modelo, int bateria, EstadoDrone estado, String zona, double capacidadCargaMax) {
        this.id = id;
        this.modelo = modelo;
        this.bateria = bateria;
        this.estado = estado;
        this.zona = zona;
        this.capacidadCargaMax = capacidadCargaMax;
    }

    public String getId() { return id; }
    public String getModelo() { return modelo; }
    public int getBateria() { return bateria; }
    public EstadoDrone getEstado() { return estado; }
    public String getZona() { return zona; }
    public double getCapacidadCargaMax() { return capacidadCargaMax; }
    
    public void setEstado(EstadoDrone estado) { this.estado = estado; }
    public void setBateria(int bateria) { this.bateria = bateria; }

    public boolean disponible() {
        return estado == EstadoDrone.DISPONIBLE;
    }

    // Abstract method to be implemented by subclasses
    public abstract String getTipo();
}
