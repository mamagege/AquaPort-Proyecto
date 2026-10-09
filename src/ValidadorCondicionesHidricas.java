public class ValidadorCondicionesHidricas extends ValidadorCadena {
    private final ProveedorCondiciones proveedor;

    public ValidadorCondicionesHidricas(ProveedorCondiciones proveedor) {
        this.proveedor = proveedor;
    }

    @Override
    protected boolean check(Mision mision) {
        CondicionesHidricas condiciones = proveedor.consultarCondiciones(mision.getPuntoPartida());
        // Validacion: turbidez < 50.0 y nivel de agua > 10.0
        return condiciones.getNivelAgua() > 10.0 && condiciones.getTurbidez() < 50.0;
    }
}
