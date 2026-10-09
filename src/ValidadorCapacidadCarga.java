public class ValidadorCapacidadCarga extends ValidadorCadena {
    @Override
    protected boolean check(Mision mision) {
        DroneAcuatico d = mision.getDrone();
        // Asumimos validacion simple: la carga requiere menos que la capacidad maxima.
        // Como TipoCarga no tiene peso explicito, simulamos que MUESTRA_AGUA requiere 0.5kg
        double pesoRequerido = mision.getTipoCarga() == TipoCarga.MUESTRA_AGUA ? 0.5 : 1.0;
        return d.getCapacidadCargaMax() >= pesoRequerido;
    }
}
