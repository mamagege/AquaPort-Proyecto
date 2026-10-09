public abstract class DroneDecorator extends DroneAcuatico {
    protected final DroneAcuatico droneEnvuelto;

    public DroneDecorator(DroneAcuatico drone) {
        // Inicializamos con los valores del drone original para mantener el contrato base
        super(drone.getId(), drone.getModelo(), drone.getBateria(), drone.getEstado(), drone.getZona(), drone.getCapacidadCargaMax());
        this.droneEnvuelto = drone;
    }

    @Override
    public String getTipo() {
        return droneEnvuelto.getTipo();
    }
    
    // Delegamos la funcionalidad base
    @Override
    public boolean disponible() {
        return droneEnvuelto.disponible();
    }
    
    // El metodo central que se decora, en v3 añadimos esta simulación
    public void registrarTelemetria() {
        droneEnvuelto.registrarTelemetria();
    }
}
