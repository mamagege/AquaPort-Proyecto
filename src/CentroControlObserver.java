public class CentroControlObserver implements DroneObserver {
    @Override
    public void onDroneFallo(DroneAcuatico drone) {
        System.out.println("[ALERTA CENTRO CONTROL] Drone en FALLO: " + drone.getId() + " en zona " + drone.getZona());
    }
}
