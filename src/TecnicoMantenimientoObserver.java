public class TecnicoMantenimientoObserver implements DroneObserver {
    @Override
    public void onDroneFallo(DroneAcuatico drone) {
        System.out.println("[ALERTA TÉCNICO] Requiere revisión inmediata: Drone " + drone.getId());
    }
}
