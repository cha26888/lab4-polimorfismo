public class FutbolSala extends Encuentro {

    public FutbolSala(Equipo equipo1, Equipo equipo2) {
        super(equipo1, equipo2);
    }

    @Override
    protected void validarMarcador(int marcador1, int marcador2) {
    }

    @Override
    protected int calcularPuntos(int propio, int rival) {
        if (propio > rival) {
            return 3;
        }
        if (propio == rival) {
            return 1;
        }
        return 0;
    }

    @Override
    public String getDeporte() {
        return "Fútbol sala";
    }
}
