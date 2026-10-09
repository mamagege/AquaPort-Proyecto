public class TecnicoMantenimientoObserver implements ObservadorMision {
    @Override
    public void onDroneFallo(DroneAcuatico drone) {
        System.out.println("[ALERTA TÉCNICO] Requiere revisión inmediata: Drone " + drone.getId());
    }
    
    @Override
    public void notificarFalloAsignacion(Mision mision) {
        // El técnico no interviene en asignaciones fallidas
    }
}
