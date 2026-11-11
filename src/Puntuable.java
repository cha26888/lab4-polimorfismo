public interface Puntuable {

    void registrarResultado(int marcador1, int marcador2);

    void validarAdmiteResultado();

    boolean estaFinalizado();

    String getDescripcion();
}
