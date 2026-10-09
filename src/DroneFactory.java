public class DroneFactory {
    public static DroneAcuatico crearDrone(String tipo, String id, String modelo, int bateria, EstadoDrone estado, String zona) {
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de drone no puede ser nulo.");
        }
        return switch (tipo.toUpperCase()) {
            case "SUPERFICIAL" -> new DroneSuperficial(id, modelo, bateria, estado, zona);
            case "SEMISUMERGIDO" -> new DroneSemisumergido(id, modelo, bateria, estado, zona);
            case "BUCEADOR" -> new DroneBuceador(id, modelo, bateria, estado, zona);
            default -> throw new IllegalArgumentException("Tipo de drone desconocido: " + tipo);
        };
    }
}
