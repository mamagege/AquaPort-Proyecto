public interface ObservadorMision {
    void notificarFalloAsignacion(Mision mision);
    void onDroneFallo(DroneAcuatico drone);
}
