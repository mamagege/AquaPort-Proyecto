public class DroneSemisumergido extends DroneAcuatico {
    public DroneSemisumergido(String id, String modelo, int bateria, EstadoDrone estado, String zona) {
        super(id, modelo, bateria, estado, zona, 1.5); // carga hasta 1.5kg
    }

    @Override
    public String getTipo() {
        return "SEMISUMERGIDO";
    }
}
