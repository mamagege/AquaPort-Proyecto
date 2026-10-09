public class DroneConMonitoreo extends DroneDecorator {
    private boolean telemetriaActiva;

    public DroneConMonitoreo(DroneAcuatico drone) {
        super(drone);
        this.telemetriaActiva = false;
    }

    @Override
    public void registrarTelemetria() {
        super.registrarTelemetria(); // Comportamiento base si lo hubiera
        this.telemetriaActiva = true;
        // Lógica adicional del decorator:
        // System.out.println("Monitoreo activado para el drone " + droneEnvuelto.getId());
    }
    
    public boolean isTelemetriaActiva() {
        return telemetriaActiva;
    }
}
