public class Waypoint {
    private final String id;
    private final String zona;
    private final double latitud;
    private final double longitud;

    public Waypoint(String id, String zona, double latitud, double longitud) {
        this.id = id;
        this.zona = zona;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public String getId() { return id; }
    public String getZona() { return zona; }
    public double getLatitud() { return latitud; }
    public double getLongitud() { return longitud; }
}
