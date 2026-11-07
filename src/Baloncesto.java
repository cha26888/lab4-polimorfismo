public class Baloncesto extends Encuentro {

    public Baloncesto(Equipo equipo1, Equipo equipo2) {
        super(equipo1, equipo2);
    }

    @Override
    protected void validarMarcador(int marcador1, int marcador2) {
        if (marcador1 == marcador2) {
            throw new IllegalArgumentException("En baloncesto no se permiten empates: el marcador no puede ser igual.");
        }
    }

    @Override
    protected int calcularPuntos(int propio, int rival) {
        if (propio > rival) {
            return 2;
        }
        return 1;
    }

    @Override
    public String getDeporte() {
        return "Baloncesto";
    }
}
