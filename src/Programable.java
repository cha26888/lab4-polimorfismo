public interface Programable extends Comparable<Programable> {

    Programacion getProgramacion();

    boolean estaProgramado();

    boolean admiteProgramacion();

    void programar(Programacion programacion);

    boolean participa(Equipo equipo);

    boolean comparteEquipoCon(Programable otro);

    String getDescripcion();

    default int compareTo(Programable otro) {
        Programacion a = getProgramacion();
        Programacion b = otro.getProgramacion();
        int resultado = a.getFecha().compareTo(b.getFecha());
        if (resultado == 0) {
            resultado = Integer.compare(a.getHora(), b.getHora());
        }
        if (resultado == 0) {
            resultado = a.getSede().compareToIgnoreCase(b.getSede());
        }
        return resultado;
    }
}
