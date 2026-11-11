public abstract class Encuentro implements Programable, Puntuable {

    private Equipo equipo1;
    private Equipo equipo2;
    private int marcador1;
    private int marcador2;
    private EstadoEncuentro estado;
    private Programacion programacion;

    protected Encuentro(Equipo equipo1, Equipo equipo2) {
        if (equipo1 == null || equipo2 == null) {
            throw new IllegalArgumentException("Un encuentro necesita dos equipos registrados.");
        }
        if (equipo1.equals(equipo2)) {
            throw new IllegalArgumentException("Un equipo no puede jugar contra sí mismo.");
        }
        this.equipo1 = equipo1;
        this.equipo2 = equipo2;
        this.marcador1 = 0;
        this.marcador2 = 0;
        this.estado = EstadoEncuentro.SIN_PROGRAMAR;
        this.programacion = null;
    }

    protected abstract void validarMarcador(int marcador1, int marcador2);

    protected abstract int calcularPuntos(int propio, int rival);

    public abstract String getDeporte();

    @Override
    public void validarAdmiteResultado() {
        if (estado == EstadoEncuentro.SIN_PROGRAMAR) {
            throw new IllegalStateException("No se puede registrar el resultado: el encuentro todavía no está programado.");
        }
        if (estado == EstadoEncuentro.FINALIZADO) {
            throw new IllegalStateException("El encuentro ya está finalizado y no admite otro resultado.");
        }
    }

    public static void validarMarcadorNoNegativo(int marcador) {
        if (marcador < 0) {
            throw new IllegalArgumentException("Los marcadores no pueden ser negativos.");
        }
    }

    @Override
    public void registrarResultado(int marcador1, int marcador2) {
        validarAdmiteResultado();
        validarMarcadorNoNegativo(marcador1);
        validarMarcadorNoNegativo(marcador2);
        validarMarcador(marcador1, marcador2);
        int puntos1 = calcularPuntos(marcador1, marcador2);
        int puntos2 = calcularPuntos(marcador2, marcador1);
        this.marcador1 = marcador1;
        this.marcador2 = marcador2;
        this.estado = EstadoEncuentro.FINALIZADO;
        equipo1.sumarPuntos(puntos1);
        equipo2.sumarPuntos(puntos2);
    }

    @Override
    public void programar(Programacion programacion) {
        if (programacion == null) {
            throw new IllegalArgumentException("La programación no puede estar vacía.");
        }
        if (!admiteProgramacion()) {
            throw new IllegalStateException("El encuentro ya tiene resultado y no puede reprogramarse.");
        }
        this.programacion = programacion;
        this.estado = EstadoEncuentro.PROGRAMADO;
    }

    @Override
    public boolean estaProgramado() {
        return programacion != null;
    }

    @Override
    public boolean admiteProgramacion() {
        return estado != EstadoEncuentro.FINALIZADO;
    }

    @Override
    public boolean participa(Equipo equipo) {
        return equipo1.equals(equipo) || equipo2.equals(equipo);
    }

    @Override
    public boolean comparteEquipoCon(Programable otro) {
        return otro.participa(equipo1) || otro.participa(equipo2);
    }

    @Override
    public boolean estaFinalizado() {
        return estado == EstadoEncuentro.FINALIZADO;
    }

    @Override
    public String getDescripcion() {
        String texto = getDeporte() + ": " + equipo1.getNombre() + " vs " + equipo2.getNombre();
        if (estado == EstadoEncuentro.FINALIZADO) {
            texto = texto + " (" + marcador1 + " - " + marcador2 + ")";
        }
        if (programacion != null) {
            texto = texto + " | " + programacion;
        } else {
            texto = texto + " | Sin programar";
        }
        return texto;
    }

    @Override
    public Programacion getProgramacion() {
        return programacion;
    }
}
