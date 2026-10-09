public class ValidadorBateria extends ValidadorCadena {
    @Override
    protected boolean check(Mision mision) {
        DroneAcuatico d = mision.getDrone();
        // La bateria debe ser > 20%
        return d.getBateria() > 20;
    }
}
