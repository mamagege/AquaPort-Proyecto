public abstract class ValidadorCadena {
    private ValidadorCadena siguiente;

    public ValidadorCadena setSiguiente(ValidadorCadena siguiente) {
        this.siguiente = siguiente;
        return siguiente; // Permite encadenar: v1.setSiguiente(v2).setSiguiente(v3);
    }

    public boolean validar(Mision mision) {
        if (!check(mision)) {
            return false;
        }
        if (siguiente != null) {
            return siguiente.validar(mision);
        }
        return true;
    }

    protected abstract boolean check(Mision mision);
}
