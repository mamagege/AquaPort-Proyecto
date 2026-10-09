public class DroneBuceador extends DroneAcuatico {
    public DroneBuceador(String id, String modelo, int bateria, EstadoDrone estado, String zona) {
        super(id, modelo, bateria, estado, zona, 0.3); // carga hasta 300g
    }

    @Override
    public String getTipo() {
        return "BUCEADOR";
    }
}
