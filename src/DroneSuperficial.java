public class DroneSuperficial extends DroneAcuatico {
    public DroneSuperficial(String id, String modelo, int bateria, EstadoDrone estado, String zona) {
        super(id, modelo, bateria, estado, zona, 0.5); // carga hasta 500g
    }

    @Override
    public String getTipo() {
        return "SUPERFICIAL";
    }
}
