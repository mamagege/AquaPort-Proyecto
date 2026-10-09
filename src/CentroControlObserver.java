public class CentroControlObserver implements ObservadorMision {
    @Override
    public void onDroneFallo(DroneAcuatico drone) {
        System.out.println("[ALERTA CENTRO CONTROL] Drone en FALLO: " + drone.getId() + " en zona " + drone.getZona());
    }
    
    @Override
    public void notificarFalloAsignacion(Mision mision) {
        System.out.println("[ALERTA CENTRO CONTROL] Misión fallida: " + mision.getId() + " sin drones disponibles.");
    }
}
