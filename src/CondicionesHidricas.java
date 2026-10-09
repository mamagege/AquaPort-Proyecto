public class CondicionesHidricas {
    private final double nivelAgua;
    private final double turbidez;

    public CondicionesHidricas(double nivelAgua, double turbidez) {
        this.nivelAgua = nivelAgua;
        this.turbidez = turbidez;
    }

    public double getNivelAgua() { return nivelAgua; }
    public double getTurbidez() { return turbidez; }
}
