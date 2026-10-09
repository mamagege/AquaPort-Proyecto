public interface APIHidricaExterna {
    /**
     * Retorna un JSON con las condiciones de la zona.
     * Ejemplo: {"waterLevel": 85.5, "turbidity": 12.0}
     */
    String obtenerDatosZona(String zona);
}
