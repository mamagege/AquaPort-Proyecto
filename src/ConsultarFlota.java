import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;

public class ConsultarFlota {
    public List<DroneAcuatico> dronesDisponibles35(List<DroneAcuatico> flota) {
        return flota.stream().filter(drone -> drone.disponible() && drone.getBateria() > 35 ).collect(Collectors.toList());

    }

    public List<String> idDronesDisponibles(List<DroneAcuatico> flota) {
        return flota.stream()
                .filter(drone -> drone.disponible())
                .map(drone -> drone.getId())
                .collect(Collectors.toList());
    }

    public boolean existeDronDisponible35(List<DroneAcuatico> flota) {
        return flota.stream().anyMatch(drone -> drone.disponible() && drone.getBateria() > 35 );
    }

    public long numDronesdisponibles(List<DroneAcuatico> flota) {
        return flota.stream()
                .filter(drone -> drone.disponible())
                .count();
    }

    public Optional<DroneAcuatico> dronMayorbateria(List<DroneAcuatico> flota) {
        return flota.stream().max(Comparator.comparingInt(drone -> drone.getBateria()));
    }

    public static void main(String[] args) {
        List<DroneAcuatico> flota = List.of(
            DroneFactory.crearDrone("SUPERFICIAL", "AR-01", "Aqua-Ranger 100", 92, EstadoDrone.DISPONIBLE, "Embalse Norte"),
            DroneFactory.crearDrone("SUPERFICIAL", "AR-02", "Aqua-Ranger 100", 45, EstadoDrone.DISPONIBLE, "Canal Central"),
            DroneFactory.crearDrone("SUPERFICIAL", "AR-03", "Aqua-Ranger 100", 18, EstadoDrone.EN_MISION, "Laguna Sur"),
            DroneFactory.crearDrone("SUPERFICIAL", "AR-04", "Aqua-Ranger 100", 73, EstadoDrone.DISPONIBLE, "Punto Ribereño Este")
        );

        System.out.println("=== Resultados de las 5 Consultas ===");
        
        ConsultarFlota consultor = new ConsultarFlota();

        List<DroneAcuatico> disp35 = consultor.dronesDisponibles35(flota);
        System.out.println("1. Drones disponibles  y > 35% batería: " + disp35);

        List<String> ids = consultor.idDronesDisponibles(flota);
        System.out.println("2. IDs de drones disponibles: " + ids);

        boolean existe = consultor.existeDronDisponible35(flota);
        System.out.println("3. ¿Existe dron disp. y > 35% batería?: " + existe);

        long cantidad = consultor.numDronesdisponibles(flota);
        System.out.println("4. Número de drones disponibles: " + cantidad);

        Optional<DroneAcuatico> dronMaxBat = consultor.dronMayorbateria(flota);
        dronMaxBat.ifPresentOrElse(
            dron -> System.out.println("5. Dron con mayor batería: " + dron.getId() + " (" + dron.getBateria() + "%)"),
            () -> System.out.println("5. Dron con mayor batería: No se encontraron drones")
        );
    }
}
