public class DroneConCifrado extends DroneDecorator {
    private boolean cifradoActivo;

    public DroneConCifrado(DroneAcuatico drone) {
        super(drone);
        this.cifradoActivo = false;
    }

    @Override
    public void registrarTelemetria() {
        super.registrarTelemetria();
        this.cifradoActivo = true;
        // System.out.println("Cifrado AES-256 activado para el drone " + droneEnvuelto.getId());
    }

    public boolean isCifradoActivo() {
        return cifradoActivo;
    }
}
