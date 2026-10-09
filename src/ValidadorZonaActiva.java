public class ValidadorZonaActiva extends ValidadorCadena {
    @Override
    protected boolean check(Mision mision) {
        // Verifica que la zona de origen del drone coincide con la mision
        return mision.getDrone().getZona().equals(mision.getPuntoPartida());
    }
}
