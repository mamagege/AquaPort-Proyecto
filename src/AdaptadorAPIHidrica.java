public class AdaptadorAPIHidrica implements ProveedorCondiciones {
    private final APIHidricaExterna api;

    public AdaptadorAPIHidrica(APIHidricaExterna api) {
        this.api = api;
    }

    public CondicionesHidricas consultarCondiciones(String zona) {
        String json = api.obtenerDatosZona(zona);
        // Simulacion de parseo JSON muy simple y robusta
        try {
            double waterLevel = extraerValor(json, "waterLevel");
            double turbidity = extraerValor(json, "turbidity");
            return new CondicionesHidricas(waterLevel, turbidity);
        } catch (Exception e) {
            // Manejo de errores de adaptacion devolviendo condiciones seguras por defecto
            return new CondicionesHidricas(0, 0);
        }
    }

    private double extraerValor(String json, String key) {
        // Busca '"key": valor'
        String searchKey = "\"" + key + "\":";
        int index = json.indexOf(searchKey);
        if (index == -1) return 0;
        
        int start = index + searchKey.length();
        int end = json.indexOf(",", start);
        if (end == -1) {
            end = json.indexOf("}", start);
        }
        if (end == -1) return 0;
        
        String valorStr = json.substring(start, end).trim();
        return Double.parseDouble(valorStr);
    }
}
